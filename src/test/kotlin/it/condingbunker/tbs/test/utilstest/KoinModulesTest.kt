package it.condingbunker.tbs.test.utilstest

import io.ktor.application.*
import it.codingbunker.tbs.data.client.TakaoMongoClient
import it.codingbunker.tbs.data.client.TakaoMongoClient.Companion.ADDRESS_PROPERTY_KEY
import it.codingbunker.tbs.data.client.TakaoMongoClient.Companion.DATABASE_NAME_PROPERTY_KEY
import it.codingbunker.tbs.data.repo.DiscordRepository
import it.codingbunker.tbs.data.repo.DiscordRepositoryInterface
import it.codingbunker.tbs.utils.getPropertyString
import it.condingbunker.tbs.test.data.client.TakaoMongoClientTest
import org.koin.core.KoinApplication
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.koin.logger.slf4jLogger

fun Application.installMockKoin(): Koin {
    stopKoin()
    uninstall(Koin)

    return install(Koin) {
        slf4jLogger()
        loadKoinModulesMock(environment)
    }
}

private fun KoinApplication.loadKoinModulesMock(environment: ApplicationEnvironment): KoinApplication {
    val dataModule = module {
        single {
            TakaoMongoClientTest(
                serverAddress = environment.config.getPropertyString(ADDRESS_PROPERTY_KEY),
                databaseName = environment.config.getPropertyString(DATABASE_NAME_PROPERTY_KEY)
            ) as TakaoMongoClient
        }
        factory<DiscordRepositoryInterface> { DiscordRepository(get()) }
    }

    return modules(listOf(dataModule))
}

