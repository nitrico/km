package dev.miguelmoreno.km.di

import dev.miguelmoreno.km.Store
import dev.miguelmoreno.km.data.RunsRepository
import dev.miguelmoreno.km.data.UserRepository
import dev.miguelmoreno.km.data.source.api.ApiDataSource
import dev.miguelmoreno.km.data.source.api.StravaApi
import dev.miguelmoreno.km.data.source.api.UserAccountStore
import dev.miguelmoreno.km.domain.LoggerMiddleware
import dev.miguelmoreno.km.domain.State
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(enableNetworkLogs: Boolean = true, appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(commonModule(enableNetworkLogs = enableNetworkLogs), platformModule())
    }

// called by iOS etc
//?fun initKoin() = initKoin(enableNetworkLogs = true) {}

fun commonModule(enableNetworkLogs: Boolean) = module {
    single { httpClient(enableNetworkLogs) }
    single { UserAccountStore(settings = get()) }
    single { StravaApi(httpClient = get(), userAccountStore = get()) }
    single { ApiDataSource(stravaApi = get())}
    single { UserRepository(apiDataSource = get(), userAccountStore = get()) }
    single { RunsRepository(apiDataSource = get()) }
    single { store() }
}

private fun httpClient(enableNetworkLogs: Boolean) =
    HttpClient {
        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        if (enableNetworkLogs) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }
    }

private fun store() = Store(
    initialState = State(),
    middleware = listOf(
        LoggerMiddleware()
    )
)

expect fun platformModule(): Module
