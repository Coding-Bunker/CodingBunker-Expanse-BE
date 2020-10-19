package it.condingbunker.tbs.test.utilstest

import io.ktor.application.*
import it.codingbunker.tbs.data.client.TakaoMongoClient
import it.codingbunker.tbs.data.client.TakaoMongoClient.Companion.ADDRESS_PROPERTY_KEY
import it.codingbunker.tbs.data.client.TakaoMongoClient.Companion.DATABASE_NAME_PROPERTY_KEY
import it.codingbunker.tbs.data.repo.DiscordRepository
import it.codingbunker.tbs.data.repo.DiscordRepositoryInterface
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
                serverAddress = environment.config.property(ADDRESS_PROPERTY_KEY).getString(),
                databaseName = environment.config.property(DATABASE_NAME_PROPERTY_KEY).getString()
            ) as TakaoMongoClient
        }
        factory<DiscordRepositoryInterface> { DiscordRepository(get()) }
    }

    return modules(listOf(dataModule))
}

