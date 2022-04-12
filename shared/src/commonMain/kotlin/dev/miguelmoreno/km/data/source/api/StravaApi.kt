package dev.miguelmoreno.km.data.source.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class StravaApi(
    private val httpClient: HttpClient,
    private val stravaApiConnectionManager: StravaApiConnectionManager,
    private val userAccountStore: UserAccountStore,
) {
    suspend fun getAthlete(): AthleteApiModel = stravaRequest(URL_REQUEST_ATHLETE) {
        header("Authorization", "Bearer ${userAccountStore.user?.accessToken}")
    }

    suspend fun getActivities(
        afterEpochInSeconds: Long,
        beforeEpochInSeconds: Long,
        perPage: Int,
        page: Int = 1
    ): List<ActivityApiModel> = stravaRequest(URL_REQUEST_ACTIVITIES) {
        header("Authorization", "Bearer ${userAccountStore.user?.accessToken}")
        parameter("after", afterEpochInSeconds)
        parameter("before", beforeEpochInSeconds)
        parameter("page", page)
        parameter("per_page", perPage)
    }

    private suspend inline fun <reified T> stravaRequest(
        url: String,
        requestConfig: HttpRequestBuilder.() -> Unit
    ): T {
        val response: HttpResponse = httpClient.get(url) {
            requestConfig(this)
        }
        return when (val code = response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.Unauthorized -> {
                stravaApiConnectionManager.refreshToken()
                httpClient.get(url) { requestConfig(this) }.body()
            }
            else -> stravaApiError(code)
        }
    }

    companion object {
        private const val URL_REQUEST_ATHLETE = "https://www.strava.com/api/v3/athlete"
        private const val URL_REQUEST_ACTIVITIES = "https://www.strava.com/api/v3/athlete/activities"
    }
}

fun stravaApiError(code: HttpStatusCode): Nothing =
    throw StravaApiException("${code.value} ${code.description}")

class StravaApiException(override val message: String? = null) : Exception(message)
