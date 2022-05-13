package dev.miguelmoreno.km.data

import kotlinx.datetime.LocalDateTime
import kotlin.math.roundToInt

data class Run(
    val id: Long,
    val distance: Int,
    val startDate: LocalDateTime,
    val movingTime: Int
)

data class RunUiModel(
    val distance: String,
    val startDate: String,
    val movingTime: String
)

fun Run.toUiModel() = RunUiModel(
    distance = distance.roundToDecimals(1).toString(),
    startDate = startDate.format(),
    movingTime = movingTime.formatTime()
)

fun Int.roundToDecimals(decimals: Int): Float {
    var dotAt = 1
    repeat(decimals) { dotAt *= 10 }
    val roundedValue = (this.toFloat()/1000 * dotAt).roundToInt()
    return (roundedValue / dotAt) + (roundedValue % dotAt).toFloat() / dotAt
}

fun Int.formatTime(): String {
    val hours: Int = this / 3600
    val minutes: Int = this % 3600 / 60
    val seconds: Int = this % 3600 % 60
    return if (hours > 0) {
        "$hours:${minutes.withInitialZero()}:${seconds.withInitialZero()}"
    } else {
        "${minutes.withInitialZero()}:${seconds.withInitialZero()}"
    }
}

fun LocalDateTime.format(): String {
    return "$year-${monthNumber.withInitialZero()}-${dayOfMonth.withInitialZero()} ${hour.withInitialZero()}:${minute.withInitialZero()}"
}

fun Int.withInitialZero(): String = if (this < 10) "0$this" else "$this"
