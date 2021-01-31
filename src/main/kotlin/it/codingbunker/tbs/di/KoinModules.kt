package it.codingbunker.tbs.di

import io.ktor.application.*
import it.codingbunker.tbs.data.client.TakaoSQLClient
import it.codingbunker.tbs.data.repo.DiscordRepository
import it.codingbunker.tbs.data.repo.DiscordRepositoryInterface
import it.codingbunker.tbs.utils.Costant.Database.ADDRESS_DB_KEY
import it.codingbunker.tbs.utils.Costant.Database.DRIVER_DB_KEY
import it.codingbunker.tbs.utils.Costant.Database.PASSWORD_DB_KEY
import it.codingbunker.tbs.utils.Costant.Database.USERNAME_DB_KEY
import it.codingbunker.tbs.utils.Costant.Jwt.ISSUER_BOT_JWT_KEY
import it.codingbunker.tbs.utils.Costant.Jwt.SECRET_BOT_JWT_KEY
import it.codingbunker.tbs.utils.Costant.Jwt.SUBJECT_BOT_JWT_KEY
import it.codingbunker.tbs.utils.Costant.Jwt.TIME_VALIDATION_JWT_KEY
import it.codingbunker.tbs.utils.Costant.Secret.AAD_CRYPT_SECRET_KEY
import it.codingbunker.tbs.utils.CryptoClient
import it.codingbunker.tbs.utils.JwtConfig
import it.codingbunker.tbs.utils.getPropertyString
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
    }

    val utilModule = module {
        single {
            JwtConfig(
                issuer = environment.config.getPropertyString(ISSUER_BOT_JWT_KEY),
                subject = environment.config.getPropertyString(SUBJECT_BOT_JWT_KEY),
                secretJWT = environment.config.getPropertyString(SECRET_BOT_JWT_KEY),
                timeValid = environment.config.getPropertyString(TIME_VALIDATION_JWT_KEY).toInt(),
                cryptoClient = get()
            )
        }

        single {
            CryptoClient(
                aadSecret = environment.config.getPropertyString(AAD_CRYPT_SECRET_KEY)
            )
        }
    }

    return modules(listOf(dataModule, utilModule))
}

