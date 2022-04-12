package dev.miguelmoreno.km.data.source.api

import dev.miguelmoreno.km.data.toUser
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class StravaApiConnectionManager(
    private val httpClient: HttpClient,
    private val userAccountStore: UserAccountStore
) {
    private var refreshTokenAttempts = 0

    suspend fun authorize(authorizationCode: String, callback: () -> Unit = {}) {
        val response: HttpResponse = httpClient.post("https://www.strava.com/oauth/token") {
            parameter("client_id", CLIENT_ID)
            parameter("client_secret", CLIENT_SECRET)
            parameter("code", authorizationCode)
            parameter("grant_type", "authorization_code")
        }
        when (val code = response.status) {
            HttpStatusCode.OK -> {
                val user = response.body<UserApiModel>().toUser()
                userAccountStore.save(user)
                callback()
            }
            //else -> Log.e("StravaApiAuthorizer", "${code.value} ${code.description}")
        }
    }

    suspend fun deauthorize(callback: () -> Unit = {}) {
        if (userAccountStore.user?.accessToken != null) {
            val response: HttpResponse = httpClient.post("https://www.strava.com/oauth/deauthorize") {
                parameter("access_token", userAccountStore.user?.accessToken)
            }
            when (val code = response.status) {
                HttpStatusCode.OK -> {
                    userAccountStore.clearUserData()
                    callback()
                }
                //else -> Log.e("StravaApiAuthorizer", "${code.value} ${code.description}")
            }
        }
    }

    suspend fun refreshToken() {
        refreshTokenAttempts++
        val response: HttpResponse = httpClient.post(URL_REFRESH_TOKEN) {
            parameter("client_id", CLIENT_ID)
            parameter("client_secret", CLIENT_SECRET)
            parameter("grant_type", "refresh_token")
            parameter("refresh_token", userAccountStore.user?.refreshToken)
        }
        when (val code = response.status) {
            HttpStatusCode.OK -> {
                val user = response.body<UserApiModel>().toUser()
                userAccountStore.save(user)
                refreshTokenAttempts = 0
            }
            else -> stravaApiError(code)
        }
    }

    companion object {
        const val CLIENT_ID = "80376"
        const val CLIENT_SECRET = "83cffd064c3d079436245e639f3b4d48bf30abc1"
        private const val URL_REFRESH_TOKEN = "https://www.strava.com/oauth/token"
        private const val REFRESH_TOKEN_MAX_ATTEMPTS = 3
        private const val REDIRECT_URI = "https://km1000.miguelmoreno.dev"
        private const val SCOPE = "read,read_all,profile:read_all,activity:read,activity:read_all"

        const val URL_AUTHORIZE = "https://www.strava.com/oauth/mobile/authorize" +
                "?client_id=$CLIENT_ID" +
                "&redirect_uri=$REDIRECT_URI" +
                "&response_type=code" +
                "&approval_prompt=auto" +
                "&scope=$SCOPE"
        /*
        val URL_AUTHORIZE = "https://www.strava.com/oauth/mobile/authorize" +
                "?client_id=80376" +
                "&redirect_uri=https%3A%2F%2Fkm1000.miguelmoreno.dev" +
                "&response_type=code" +
                "&approval_prompt=auto" +
                "&scope=read%2Cread_all%2Cprofile%3Aread_all%2Cactivity%3Aread%2Cactivity%3Aread_all"
        /*
        val URL_AUTHORIZE = Uri.parse("https://www.strava.com/oauth/mobile/authorize" +
                "?client_id=${CLIENT_ID}" +
                "&redirect_uri=https://km1000.miguelmoreno.dev")
            .buildUpon()
            .appendQueryParameter("client_id", StravaApiConnectionManager.)
            .appendQueryParameter("redirect_uri", )
            .appendQueryParameter("response_type", "code")
            .appendQueryParameter("approval_prompt", "auto")
            .appendQueryParameter("scope", "read,read_all,profile:read_all,activity:read,activity:read_all")
            .build()*/
         */
    }
}
