package it.github.codingbunker.tbs.common.di

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import it.github.codingbunker.tbs.common.remote.ExpanseApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(enableNetworkLogs: Boolean = false, baseUrl: String, appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(commonModule(enableNetworkLogs = enableNetworkLogs, baseUrl = baseUrl)/*, platformModule()*/)
    }

// called by iOS etc
//fun initKoin() = initKoin(enableNetworkLogs = false) {}

fun commonModule(enableNetworkLogs: Boolean, baseUrl: String) = module {
    single { createJson() }
    single { createHttpClient(get(), enableNetworkLogs = enableNetworkLogs) }

    single { CoroutineScope(Dispatchers.Default + SupervisorJob()) }

//    single<PeopleInSpaceRepositoryInterface> { PeopleInSpaceRepository() }
//
    single { ExpanseApi(get(), baseUrl) }
}

fun createJson() = KotlinxSerializer(Json {
    prettyPrint = true
    isLenient = true
})


fun createHttpClient(httpClientEngine: HttpClientEngine, enableNetworkLogs: Boolean) =
    HttpClient(httpClientEngine) {
        install(JsonFeature) {
            serializer = createJson()
        }

        if (enableNetworkLogs) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.INFO
            }
        }
    }