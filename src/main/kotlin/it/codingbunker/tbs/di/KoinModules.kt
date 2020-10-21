package it.codingbunker.tbs.di

import io.ktor.application.*
import it.codingbunker.tbs.data.client.TakaoMongoClient
import it.codingbunker.tbs.data.client.TakaoMongoClient.Companion.ADDRESS_PROPERTY_KEY
import it.codingbunker.tbs.data.repo.DiscordRepository
import it.codingbunker.tbs.data.repo.DiscordRepositoryInterface
import it.codingbunker.tbs.utils.getPropertyString
import org.koin.core.KoinApplication
import org.koin.dsl.module

fun KoinApplication.loadKoinModules(environment: ApplicationEnvironment): KoinApplication {
    val dataModule = module {
        single {
            TakaoMongoClient(
                serverAddress = environment.config.getPropertyString(ADDRESS_PROPERTY_KEY),
                databaseName = environment.config.getPropertyString(TakaoMongoClient.DATABASE_NAME_PROPERTY_KEY)
            )
        }
        factory<DiscordRepositoryInterface> { DiscordRepository(get()) }
    }

    return modules(listOf(dataModule))
}

