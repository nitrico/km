package dev.miguelmoreno.km.android

import android.app.Application
import co.touchlab.kermit.Logger
import dev.miguelmoreno.km.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import org.koin.dsl.module

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            // https://github.com/InsertKoinIO/koin/issues/1188
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@App)
            modules(appModule)
        }

        Logger.withTag("App").d { "App started" }
    }

    private val appModule = module {

    }
}
