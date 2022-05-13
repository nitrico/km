package dev.miguelmoreno.km.data.source.api

import dev.miguelmoreno.km.data.Run
import dev.miguelmoreno.km.data.User
import kotlinx.datetime.*

class ApiDataSource(private val stravaApi: StravaApi) {

    suspend fun getUser(accessToken: String, refreshToken: String): User = runCatching {
        stravaApi.getAthlete()
    }.fold(
        onSuccess = { athlete -> athlete.toUser(accessToken, refreshToken) },
        onFailure = { throw NotAvailableException(it.message) }
    )

    // TODO: PAGING is missing!
    suspend fun getRuns(): List<Run> = runCatching {
        stravaApi.getActivities(beginningOfTheYearEpochInSeconds, nowEpochInSeconds, 200)
    }.fold(
        onSuccess = { activities ->
            activities.filter { it.type == ACTIVITY_TYPE_RUN }.map { it.toRun() }
        },
        onFailure = { throw NotAvailableException(it.message) }
    )

    private val nowEpochInSeconds: Long
        get() = Clock.System.now().toEpochMilliseconds() / 1000

    private val beginningOfTheYearEpochInSeconds: Long
        get() {
            val timeZone = TimeZone.UTC
            val currentDateTime = Clock.System.now().toLocalDateTime(timeZone)
            val year = currentDateTime.year
            val beginningOfYear = "$year-01-01"
            return LocalDate.parse(beginningOfYear).atStartOfDayIn(timeZone).toEpochMilliseconds() / 1000
        }

    class NotAvailableException(override val message: String? = null) : Exception(message)
}
