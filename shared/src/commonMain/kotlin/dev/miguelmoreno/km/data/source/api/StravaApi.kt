package dev.miguelmoreno.km.data.source.api

import co.touchlab.kermit.Logger
import dev.miguelmoreno.km.BuildKonfig
import dev.miguelmoreno.km.data.Token
import dev.miguelmoreno.km.data.toUser
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class StravaApi(
    private val httpClient: HttpClient,
    private val userAccountStore: UserAccountStore,
) {
    private val logger = Logger.withTag("StravaApi")

    private var refreshTokenAttempts = 0

    private val accessToken: String?
        get() {
            val token = userAccountStore.user?.accessToken?.value
            if (token == null) {
                logger.e("Access token cannot be null")
            }
            return token
        }

    suspend fun getAthlete(): AthleteApiModel = request {
        method = HttpMethod.Get
        url(URL_REQUEST_ATHLETE)
        header("Authorization", "Bearer $accessToken")
    }.execute { body() }

    suspend fun getActivities(
        afterEpochInSeconds: Long,
        beforeEpochInSeconds: Long,
        perPage: Int = 200,
        page: Int = 1
    ): List<ActivityApiModel> = request {
        method = HttpMethod.Get
        url(URL_REQUEST_ACTIVITIES)
        header("Authorization", "Bearer $accessToken")
        parameter("after", afterEpochInSeconds)
        parameter("before", beforeEpochInSeconds)
        parameter("page", page)
        parameter("per_page", perPage)
    }.execute { body() }

    suspend fun authorize(authorizationCode: String, callback: () -> Unit = {}) = request {
        method = HttpMethod.Post
        url(URL_AUTH_TOKEN)
        parameter("client_id", CLIENT_ID)
        parameter("client_secret", CLIENT_SECRET)
        parameter("code", authorizationCode)
        parameter("grant_type", "authorization_code")
    }.execute {
        val user = body<UserApiModel>().toUser()
        userAccountStore.save(user)
        callback()
    }

    suspend fun deauthorize(callback: () -> Unit = {}) = request {
        method = HttpMethod.Post
        url(URL_AUTH_DEAUTHORIZE)
        parameter("access_token", accessToken)
    }.execute {
        userAccountStore.clearUserData()
        callback()
    }

    private suspend fun refreshToken() = request {
        method = HttpMethod.Post
        url(URL_AUTH_TOKEN)
        parameter("client_id", CLIENT_ID)
        parameter("client_secret", CLIENT_SECRET)
        parameter("grant_type", "refresh_token")
        parameter("refresh_token", userAccountStore.user?.refreshToken?.value)
    }.execute {
        val tokens = body<RefreshTokenResponseApiModel>()
        val user = requireNotNull(userAccountStore.user).copy(
            accessToken = Token(tokens.accessToken),
            refreshToken = Token(tokens.refreshToken)
        )
        userAccountStore.save(user)
        refreshTokenAttempts = 0
    }

    private suspend fun <T> HttpRequestBuilder.execute(okBlock: suspend HttpResponse.() -> T): T =
        httpClient.request(this).handle(this, okBlock)

    private suspend fun <T> HttpResponse.handle(
        httpRequest: HttpRequestBuilder,
        okBlock: suspend HttpResponse.() -> T
    ): T = when (status) {
        HttpStatusCode.OK -> okBlock()
        HttpStatusCode.Unauthorized -> refreshTokenAndRetry(httpRequest, okBlock)
        else -> stravaApiError(status)
    }

    private suspend  fun <T> refreshTokenAndRetry(
        request: HttpRequestBuilder,
        okBlock: suspend HttpResponse.() -> T
    ): T {
        refreshToken()
        return httpClient.request(request).handle(request, okBlock)
    }

    private fun stravaApiError(code: HttpStatusCode): Nothing =
        throw RequestException("${code.value} ${code.description}")

    class RequestException(override val message: String? = null) : Exception(message)

    companion object {
        private const val URL_REQUEST_ATHLETE = "https://www.strava.com/api/v3/athlete"
        private const val URL_REQUEST_ACTIVITIES = "https://www.strava.com/api/v3/athlete/activities"
        private const val URL_AUTH_TOKEN = "https://www.strava.com/oauth/token"
        private const val URL_AUTH_DEAUTHORIZE = "https://www.strava.com/oauth/deauthorize"

        private const val CLIENT_ID = "80376"
        private val CLIENT_SECRET = BuildKonfig.STRAVA_CLIENT_SECRET
        private const val REFRESH_TOKEN_MAX_ATTEMPTS = 3
        private const val REDIRECT_URI = "https://km1000.miguelmoreno.dev"
        private const val SCOPE = "read,read_all,profile:read_all,activity:read,activity:read_all"

        const val URL_AUTHORIZE = "https://www.strava.com/oauth/mobile/authorize" +
                "?client_id=$CLIENT_ID" +
                "&redirect_uri=$REDIRECT_URI" +
                "&response_type=code" +
                "&approval_prompt=auto" +
                "&scope=$SCOPE"
    }
}
