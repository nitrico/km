package dev.miguelmoreno.km.data.source.api

import com.russhwolf.settings.Settings
import dev.miguelmoreno.km.data.Token
import dev.miguelmoreno.km.data.User

class UserAccountStore(
    private val settings: Settings
) {
    val user: User?
        get() = with(settings) {
            User(
                id = getStringOrNull(USER_ID) ?: return null,
                username = getStringOrNull(USERNAME) ?: return null,
                firstName = getString(USER_FIRST_NAME),
                lastName = getString(USER_LAST_NAME),
                profilePicture = getString(USER_PROFILE_PICTURE),
                accessToken = Token(getStringOrNull(ACCESS_TOKEN) ?: return null),
                refreshToken = Token(getStringOrNull(REFRESH_TOKEN) ?: return null)
            )
        }

    fun save(user: User) = with(settings) {
        putString(USERNAME, user.username)
        putString(USER_ID, user.id)
        putString(USER_FIRST_NAME, user.firstName)
        putString(USER_LAST_NAME, user.lastName)
        putString(USER_PROFILE_PICTURE, user.profilePicture)
        putString(ACCESS_TOKEN, user.accessToken.value)
        putString(REFRESH_TOKEN, user.refreshToken.value)
    }

    fun clearUserData() {
        settings.clear()
    }

    private companion object {
        private const val USERNAME = "dev.miguelmoreno.km.USERNAME"
        private const val USER_ID = "dev.miguelmoreno.km.USER_ID"
        private const val USER_FIRST_NAME = "dev.miguelmoreno.km.USER_FIRST_NAME"
        private const val USER_LAST_NAME = "dev.miguelmoreno.km.USER_LAST_NAME"
        private const val USER_PROFILE_PICTURE = "dev.miguelmoreno.km.USER_PROFILE_PICTURE"
        private const val ACCESS_TOKEN = "dev.miguelmoreno.km.STRAVA_ACCESS_TOKEN"
        private const val REFRESH_TOKEN = "dev.miguelmoreno.km.STRAVA_REFRESH_TOKEN"
    }
}
