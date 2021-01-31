package it.condingbunker.tbs.test.utilstest

import io.ktor.application.*
import it.codingbunker.tbs.data.client.TakaoSQLClient
import it.codingbunker.tbs.data.repo.DiscordRepository
import it.codingbunker.tbs.data.repo.DiscordRepositoryInterface
import it.codingbunker.tbs.utils.Costant.Database.ADDRESS_DB_KEY
import it.codingbunker.tbs.utils.Costant.Database.DRIVER_DB_KEY
import it.codingbunker.tbs.utils.Costant.Database.PASSWORD_DB_KEY
import it.codingbunker.tbs.utils.Costant.Database.USERNAME_DB_KEY
import it.codingbunker.tbs.utils.getPropertyString
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
            TakaoSQLClient(
                serverAddress = environment.config.getPropertyString(ADDRESS_DB_KEY),
                usernameDB = environment.config.getPropertyString(USERNAME_DB_KEY),
                passwordDB = environment.config.getPropertyString(PASSWORD_DB_KEY),
                driverDB = environment.config.getPropertyString(DRIVER_DB_KEY)
            )
        }
        factory<DiscordRepositoryInterface> { DiscordRepository() }
    }

    return modules(listOf(dataModule))
}

