package it.condingbunker.tbs.test.utilstest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.server.engine.*
import io.ktor.server.testing.*
import it.codingbunker.tbs.data.table.Bot
import it.codingbunker.tbs.data.table.Role
import it.codingbunker.tbs.data.table.RoleType
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory
import java.util.*

const val CONFIG_NAME: String = "application-test.conf"

val jacksonMapper = jacksonObjectMapper()

fun <R> withRealTestApplication(
    moduleFunction: Application.() -> Unit,
    configure: TestApplicationEngine.Configuration.() -> Unit = {},
    test: TestApplicationEngine.() -> R
): R {
    return withApplication(createRealTestEnvironment(), configure) {
        moduleFunction(application)
        test()
    }
}

fun createRealTestEnvironment(
    confName: String = CONFIG_NAME,
    configure: ApplicationEngineEnvironmentBuilder.() -> Unit = {}
): ApplicationEngineEnvironment =
    applicationEngineEnvironment {
        config = loadConfig(confName)
        log = LoggerFactory.getLogger("ktor.test")
        configure()
    }

fun loadConfig(confName: String = CONFIG_NAME) = HoconApplicationConfig(ConfigFactory.load(confName))

fun Map<String, Any>.toJson(): String = jacksonMapper.writeValueAsString(this)

fun Any.toJson(): String = jacksonMapper
    .writeValueAsString(this)

fun getBotMock(): Bot {
    var botEntity: Bot? = null

    transaction {
        botEntity = Bot.new(UUID.randomUUID()) {
            botName = "Bot Test"
            botKey = "Bot Key"
            botRoles = SizedCollection(listOf(Role.findById(RoleType.BOT_DISCORD)!!))
        }
    }

    return botEntity!!
}