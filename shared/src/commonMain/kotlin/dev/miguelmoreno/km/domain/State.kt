package dev.miguelmoreno.km.domain

import dev.miguelmoreno.km.data.Run
import dev.miguelmoreno.km.data.User

data class State(
    val user: User? = null,
    val runs: List<Run> = emptyList(),
    val distance: Int = 0,
    val isLoading: Boolean = false,
)

/*  val dayOfYear: Int,
    val remainingDays: Int,
    val remainingWeeks: Int,
    val distance: Int,
    val remainingDistance: Int,
    val remainingDistancePerDay: Float,
    val remainingDistancePerWeek: Float
) {

    companion object {
        fun from(distance: Int): MainState {
            val now: Instant = Clock.System.now()
            val remainingDistance = 1000 - distance
            val dayOfYear: Int = now.toLocalDateTime(TimeZone.currentSystemDefault()).dayOfYear
            val remainingDays: Int = 365 - dayOfYear
            val remainingWeeks: Int = remainingDays / 7
            return MainState(
                dayOfYear = dayOfYear,
                remainingDays = remainingDays,
                remainingWeeks = remainingWeeks,
                distance = distance,
                remainingDistance = remainingDistance,
                remainingDistancePerDay = remainingDistance / remainingDays.toFloat(),
                remainingDistancePerWeek = remainingDistance / remainingWeeks.toFloat()
            )
        }
    }
}
*/
