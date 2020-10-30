package it.codingbunker.tbs.di

import io.ktor.application.*
import it.codingbunker.tbs.data.client.TakaoSQLClient
import it.codingbunker.tbs.data.repo.DiscordRepository
import it.codingbunker.tbs.data.repo.DiscordRepositoryInterface
import it.codingbunker.tbs.utils.getPropertyString
import org.koin.core.KoinApplication
import org.koin.dsl.module

fun KoinApplication.loadKoinModules(environment: ApplicationEnvironment): KoinApplication {
    val dataModule = module {
        single {
            TakaoSQLClient(
                serverAddress = environment.config.getPropertyString(TakaoSQLClient.ADDRESS_DB_KEY),
                usernameDB = environment.config.getPropertyString(TakaoSQLClient.USERNAME_DB_KEY),
                passwordDB = environment.config.getPropertyString(TakaoSQLClient.PASSWORD_DB_KEY),
                driverDB = environment.config.getPropertyString(TakaoSQLClient.DRIVER_DB_KEY)
            )
        }
        factory<DiscordRepositoryInterface> { DiscordRepository(get()) }
    }

    return modules(listOf(dataModule))
}

