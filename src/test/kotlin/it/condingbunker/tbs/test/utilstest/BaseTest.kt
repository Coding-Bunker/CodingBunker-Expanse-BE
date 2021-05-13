package it.condingbunker.tbs.test.utilstest

import io.ktor.application.*
import io.ktor.config.*
import it.codingbunker.tbs.common.Constants
import it.codingbunker.tbs.common.client.TakaoSQLClient
import it.codingbunker.tbs.common.extension.getPropertyString
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.BeforeTest

abstract class BaseTest {
    private lateinit var takaoSQLClient: TakaoSQLClient
    private lateinit var config: HoconApplicationConfig

    @BeforeTest
    fun `Clear DB For Test`() {
        if (::config.isInitialized.not()) {
            config = loadConfig()
        }

        if (::takaoSQLClient.isInitialized.not()) {
            takaoSQLClient = TakaoSQLClient(
                serverAddress = config.getPropertyString(Constants.Database.ADDRESS_DB_KEY),
                usernameDB = config.getPropertyString(Constants.Database.USERNAME_DB_KEY),
                passwordDB = config.getPropertyString(Constants.Database.PASSWORD_DB_KEY),
                driverDB = config.getPropertyString(Constants.Database.DRIVER_DB_KEY)
            )
        }

        runBlocking {
            transaction {
                // TODO Sistemare Funzione per adeguamento in base al dialetto
                TransactionManager.current().exec("DROP ALL OBJECTS")
            }
        }
    }

    abstract fun Application.installModuleToTest()
}
