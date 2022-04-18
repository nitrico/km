package dev.miguelmoreno.km.data.source.api

import dev.miguelmoreno.km.data.Run
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

const val ACTIVITY_TYPE_RUN = "Run"

@Serializable
data class ActivityApiModel(
    val id: Long,
    val distance: Float,
    val type: String,
    @SerialName("start_date") @Serializable(MyLocalDateTimeSerializer::class) val startDate: LocalDateTime,
    @SerialName("moving_time") val movingTime: Int
)

fun ActivityApiModel.toRun(): Run {
    check(type == ACTIVITY_TYPE_RUN)
    return Run(
        id = id,
        distance = distance.toInt(),
        startDate = startDate,
        movingTime = movingTime
    )
}

@Serializable
data class UserApiModel(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
    val athlete: AthleteApiModel
)

@Serializable
data class AthleteApiModel(
    val id: String,
    val username: String,
    @SerialName("firstname") val firstName: String,
    @SerialName("lastname") val lastName: String,
    val profile: String,
)

@Serializable
data class RefreshTokenResponseApiModel(
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String
)

/**
 * Copied and modified from [kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer]
 * The only difference is to remove the 'Z' at the end
 */
object MyLocalDateTimeSerializer: KSerializer<LocalDateTime> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDateTime =
        LocalDateTime.parse(decoder.decodeString().replace("Z",""))

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.toString())
    }
}
