package it.github.codingbunker.tbs.common.di

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import it.github.codingbunker.tbs.common.BuildConfigGenerated
import it.github.codingbunker.tbs.common.Constant
import it.github.codingbunker.tbs.common.remote.ExpanseApi
import it.github.codingbunker.tbs.common.repository.CookieRepository
import it.github.codingbunker.tbs.common.repository.ExpanseRepository
import it.github.codingbunker.tbs.common.repository.ExpanseRepositoryImpl
import it.github.codingbunker.tbs.common.repository.platformModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

fun initKoin(enableNetworkLogs: Boolean = false, appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        modules(commonModule(enableNetworkLogs = enableNetworkLogs), platformModule())
        appDeclaration()
    }

// called by iOS etc
//fun initKoin() = initKoin(enableNetworkLogs = false) {}

fun commonModule(enableNetworkLogs: Boolean) = module {
    single { createJson() }
    single { createHttpClient(get(), enableNetworkLogs = enableNetworkLogs, get()) }

    single { Dispatchers.Default + SupervisorJob() } bind CoroutineContext::class

    single { CookieRepository() }
    single { ExpanseRepositoryImpl(get(), get()) } bind ExpanseRepository::class

    single { ExpanseApi(get()) }
}

fun createJson() = KotlinxSerializer(Json {
    prettyPrint = true
    isLenient = true
})


fun createHttpClient(
    httpClientEngine: HttpClientEngine,
    enableNetworkLogs: Boolean,
    cookieRepository: CookieRepository
) =
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

        defaultRequest {
            host = URLBuilder(BuildConfigGenerated.SERVER_URL)
                .host
            port = BuildConfigGenerated.SERVER_PORT.toInt()

            headers {
                val cookieKey = cookieRepository.getCookie()
                if (cookieKey != null) {
                    header(Constant.Session.LOGIN_SESSION_USER, cookieKey)
                }
            }
        }
    }