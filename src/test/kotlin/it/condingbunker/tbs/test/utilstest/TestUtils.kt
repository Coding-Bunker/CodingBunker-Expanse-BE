package it.condingbunker.tbs.test.utilstest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.testing.*
import it.codingbunker.tbs.data.dto.ProfileDTO
import it.codingbunker.tbs.data.dto.request.LoginRequest
import it.codingbunker.tbs.data.table.Bot
import it.codingbunker.tbs.data.table.Role
import it.codingbunker.tbs.data.table.RoleType
import it.codingbunker.tbs.utils.Costant
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.transactions.transaction
import org.slf4j.LoggerFactory

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

fun Map<String, Any>.toJson() = jacksonMapper.writeValueAsString(this)

fun Any.toJson() = jacksonMapper
    .writeValueAsString(this)

fun TestApplicationEngine.addBotAndGetJWT(): String {
    var botId = ""

    transaction {
        val role = Role.new(RoleType.BOT) {
            this.permissionName = RoleType.BOT
        }

        commit()

        botId = Bot.new {
            this.botName = "Bot Test"
            this.permissions = SizedCollection(listOf(role))
        }.id.value.toString()
    }

    handleRequest(HttpMethod.Post, "${Costant.Url.BASE_API_URL}/auth/login") {
        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        setBody(
            LoginRequest(botId, ProfileDTO.ProfileType.BOT.name).toJson()
        )
    }.apply {
        return "Bearer ${this.response.content}"
    }
}

fun TestApplicationEngine.handleRequest(
    method: HttpMethod,
    uri: String,
    jwt: String,
    setup: TestApplicationRequest.() -> Unit = {}
): TestApplicationCall {
    return handleRequest(method, uri) {
        setup()
        addHeader(HttpHeaders.Authorization, jwt)
    }
}