package it.codingbunker.tbs.common.di

import io.ktor.application.*
import it.codingbunker.tbs.common.Costant.Authentication.ADMIN_USER_ID
import it.codingbunker.tbs.common.Costant.Database.ADDRESS_DB_KEY
import it.codingbunker.tbs.common.Costant.Database.DRIVER_DB_KEY
import it.codingbunker.tbs.common.Costant.Database.PASSWORD_DB_KEY
import it.codingbunker.tbs.common.Costant.Database.USERNAME_DB_KEY
import it.codingbunker.tbs.common.Costant.Secret.AAD_CRYPT_SECRET_KEY
import it.codingbunker.tbs.common.client.TakaoSQLClient
import it.codingbunker.tbs.common.client.discord.OAuth2DiscordClient
import it.codingbunker.tbs.common.getProperty
import it.codingbunker.tbs.common.getPropertyList
import it.codingbunker.tbs.data.repo.DiscordRepository
import it.codingbunker.tbs.data.repo.DiscordRepositoryInterface
import it.codingbunker.tbs.feature.managment.repository.BotRepository
import it.codingbunker.tbs.feature.managment.repository.BotRepositoryImpl
import it.codingbunker.tbs.feature.managment.repository.UserRepository
import it.codingbunker.tbs.feature.managment.repository.UserRepositoryImpl
import it.codingbunker.tbs.utils.CryptoClientInterface
import it.codingbunker.tbs.utils.CryptoClientInterfaceImpl
import org.koin.core.KoinApplication
import org.koin.dsl.module

fun KoinApplication.loadKoinModules(environment: ApplicationEnvironment): KoinApplication {
    val dataModule = module {
        single {
            TakaoSQLClient(
                serverAddress = environment.getProperty(ADDRESS_DB_KEY),
                usernameDB = environment.getProperty(USERNAME_DB_KEY),
                passwordDB = environment.getProperty(PASSWORD_DB_KEY),
                driverDB = environment.getProperty(DRIVER_DB_KEY)
            )
        }
        factory<DiscordRepositoryInterface> { DiscordRepository() }
        factory<BotRepository> { BotRepositoryImpl() }
        factory<UserRepository> {
            UserRepositoryImpl(
                environment.getPropertyList(ADMIN_USER_ID)
            )
        }
    }

    val utilModule = module {
        single<CryptoClientInterface> {
            CryptoClientInterfaceImpl(
                aadSecret = environment.getProperty(AAD_CRYPT_SECRET_KEY)
            )
        }

        single {
            OAuth2DiscordClient()
        }
    }

    return modules(listOf(dataModule, utilModule))
}
