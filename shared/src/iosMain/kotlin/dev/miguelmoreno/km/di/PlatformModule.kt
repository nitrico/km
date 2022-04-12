package dev.miguelmoreno.km.di

import com.russhwolf.settings.ExperimentalSettingsImplementation
import com.russhwolf.settings.KeychainSettings
import com.russhwolf.settings.Settings
import org.koin.dsl.module

@OptIn(ExperimentalSettingsImplementation::class)
actual fun platformModule() = module {
    single<Settings> { KeychainSettings(service = "encrypted_settings") }
}
