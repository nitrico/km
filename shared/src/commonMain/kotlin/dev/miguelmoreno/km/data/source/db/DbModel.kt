package dev.miguelmoreno.km.data.source.db

import dev.miguelmoreno.km.data.Run
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class RunDbModel : RealmObject {
    @PrimaryKey
    var id: Long = 0
    var distance: Int = 0
    var startDate: Long = 0
    var movingTime: Int = 0
}

fun Run.toRunDbModel() = RunDbModel().apply {
    id = this@toRunDbModel.id
    distance = this@toRunDbModel.distance
    startDate = this@toRunDbModel.startDate.toInstant(TimeZone.UTC).epochSeconds
    movingTime = this@toRunDbModel.movingTime
}

fun RunDbModel.toRun(): Run = Run(
    id = this@toRun.id,
    distance = this@toRun.distance,
    startDate = Instant.fromEpochSeconds(this@toRun.startDate).toLocalDateTime(TimeZone.UTC),
    movingTime = this@toRun.movingTime
)
