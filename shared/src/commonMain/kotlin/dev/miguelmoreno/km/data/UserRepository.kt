package dev.miguelmoreno.km.data

import dev.miguelmoreno.km.data.source.api.ApiDataSource
import dev.miguelmoreno.km.data.source.api.UserAccountStore
import kotlin.jvm.JvmInline

class UserRepository(
    private val apiDataSource: ApiDataSource,
    private val userAccountStore: UserAccountStore
) {
    suspend fun getUser(): User? {
        var user = userAccountStore.user
        //try {
        if (user != null) {
            // refresh
            user = apiDataSource.getUser(user.accessToken.value, user.refreshToken.value)
            userAccountStore.save(user)
        }
        //} catch (exception: Exception) {

        //}
        return user
    }
}

data class User(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val profilePicture: String,
    val accessToken: Token,
    val refreshToken: Token,
)

@JvmInline
value class Token(val value: String) {
    override fun toString(): String = "t0k€n"
}

/*
$ http GET "https://www.strava.com/api/v3/athlete" "Authorization: Bearer [[token]]"

{
  "id" : 1234567890987654321,
  "username" : "marianne_t",
  "resource_state" : 3,
  "firstname" : "Marianne",
  "lastname" : "Teutenberg",
  "city" : "San Francisco",
  "state" : "CA",
  "country" : "US",
  "sex" : "F",
  "premium" : true,
  "created_at" : "2017-11-14T02:30:05Z",
  "updated_at" : "2018-02-06T19:32:20Z",
  "badge_type_id" : 4,
  "profile_medium" : "https://xxxxxx.cloudfront.net/pictures/athletes/123456789/123456789/2/medium.jpg",
  "profile" : "https://xxxxx.cloudfront.net/pictures/athletes/123456789/123456789/2/large.jpg",
  "friend" : null,
  "follower" : null,
  "follower_count" : 5,
  "friend_count" : 5,
  "mutual_friend_count" : 0,
  "athlete_type" : 1,
  "date_preference" : "%m/%d/%Y",
  "measurement_preference" : "feet",
  "clubs" : [ ],
  "ftp" : null,
  "weight" : 0,
  "bikes" : [ {
    "id" : "b12345678987655",
    "primary" : true,
    "name" : "EMC",
    "resource_state" : 2,
    "distance" : 0
  } ],
  "shoes" : [ {
    "id" : "g12345678987655",
    "primary" : true,
    "name" : "adidas",
    "resource_state" : 2,
    "distance" : 4904
  } ]
}
 */

/*    val dayOfYear: Int,
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
