package dev.miguelmoreno.km.di

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.russhwolf.settings.AndroidSettings
import com.russhwolf.settings.Settings
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.Scope
import org.koin.dsl.module

actual fun platformModule() = module {
    single<Settings> { createAndroidSettings() }
}

private fun Scope.createAndroidSettings(): Settings {
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    val encryptedSharedPreferences = EncryptedSharedPreferences.create(
        "encrypted_settings",
        masterKeyAlias,
        androidContext(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    return AndroidSettings(encryptedSharedPreferences, false)
}
