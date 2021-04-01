package it.codingbunker.tbs.common.di

import io.ktor.application.*
import it.codingbunker.tbs.common.Costant.Database.ADDRESS_DB_KEY
import it.codingbunker.tbs.common.Costant.Database.DRIVER_DB_KEY
import it.codingbunker.tbs.common.Costant.Database.PASSWORD_DB_KEY
import it.codingbunker.tbs.common.Costant.Database.USERNAME_DB_KEY
import it.codingbunker.tbs.common.Costant.Secret.AAD_CRYPT_SECRET_KEY
import it.codingbunker.tbs.common.client.TakaoSQLClient
import it.codingbunker.tbs.common.extension.getPropertyString
import it.codingbunker.tbs.data.repo.DiscordRepository
import it.codingbunker.tbs.data.repo.DiscordRepositoryInterface
import it.codingbunker.tbs.feature.managment.repository.BotRepository
import it.codingbunker.tbs.feature.managment.repository.BotRepositoryImpl
import it.codingbunker.tbs.utils.CryptoClientInterface
import it.codingbunker.tbs.utils.CryptoClientInterfaceImpl
import org.koin.core.KoinApplication
import org.koin.dsl.module

fun KoinApplication.loadKoinModules(environment: ApplicationEnvironment): KoinApplication {
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
        factory<BotRepository> { BotRepositoryImpl() }
    }

    val utilModule = module {
        single<CryptoClientInterface> {
            CryptoClientInterfaceImpl(
                aadSecret = environment.config.getPropertyString(AAD_CRYPT_SECRET_KEY)
            )
        }
    }

    return modules(listOf(dataModule, utilModule))
}
