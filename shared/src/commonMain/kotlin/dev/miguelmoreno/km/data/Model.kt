package dev.miguelmoreno.km.data

import kotlinx.datetime.LocalDateTime

data class Run(
    val id: Long,
    val distance: Int,
    val startDate: LocalDateTime,
    val movingTime: Int
)
