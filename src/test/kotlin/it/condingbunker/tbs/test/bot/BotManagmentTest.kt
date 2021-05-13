package it.condingbunker.tbs.test.bot

import io.ktor.application.*
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.testing.*
import it.codingbunker.tbs.common.Costant
import it.codingbunker.tbs.feature.managment.route.botManagmentRoutes
import it.codingbunker.tbs.feature.managment.table.*
import it.codingbunker.tbs.mainModule
import it.condingbunker.tbs.test.utilstest.BaseTest
import it.condingbunker.tbs.test.utilstest.getBotMock
import it.condingbunker.tbs.test.utilstest.toJson
import it.condingbunker.tbs.test.utilstest.withRealTestApplication
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Nested
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BotManagmentTest : BaseTest() {
    private lateinit var config: HoconApplicationConfig

    private fun TestApplicationEngine.handleRequest(
        method: HttpMethod,
        uri: String,
        bot: Bot,
        setup: TestApplicationRequest.() -> Unit = {}
    ): TestApplicationCall {
        val basic = Base64.getEncoder().encodeToString(
            "${bot.id}:${bot.botKey}".toByteArray()
        )
        return handleRequest(method, uri) {
            setup()
            addHeader(HttpHeaders.Authorization, "Basic $basic")
        }
    }

    override fun Application.installModuleToTest() {
        mainModule(true)
        botManagmentRoutes(true)
    }

    private fun getMockBotAdmin(): Bot {
        var botEntity: Bot? = null

        runBlocking {
            transaction {
                botEntity = Bot.new(UUID.randomUUID()) {
                    botKey = "abc"
                    botName = "abc"
                    botRoles = SizedCollection(Role[RoleType.ADMIN])
                }
            }
        }

        return botEntity!!
    }

    @Nested
    inner class BotCreateTest {
        @Test
        fun `Create Bot with all parameters, Result is created`() {
            withRealTestApplication({
                installModuleToTest()
            }) {
                val bot = getMockBotAdmin()

                val request = mapOf(
                    "botName" to "Bot Test",
                    "roleList" to listOf(
                        "ADMIN"
                    )
                ).toJson()

                handleRequest(HttpMethod.Put, "${Costant.Url.BASE_API_URL}/managment/bot/edit", bot) {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(request)
                }.apply {
                    assertEquals(HttpStatusCode.Created, response.status())
                }
            }
        }

        @Test
        fun `Create Bot with botName blank, Result is BadRequest`() {
            withRealTestApplication({
                installModuleToTest()
            }) {
                val bot = getMockBotAdmin()

                val request = mapOf(
                    "botName" to "",
                    "roleList" to listOf(
                        "ADMIN"
                    )
                ).toJson()

                handleRequest(HttpMethod.Put, "${Costant.Url.BASE_API_URL}/managment/bot/edit", bot) {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(request)
                }.apply {
                    assertEquals(HttpStatusCode.BadRequest, response.status())
                }
            }
        }

        @Test
        fun `Create Bot with roleList empty, Result is BadRequest`() {
            withRealTestApplication({
                installModuleToTest()
            }) {
                val bot = getMockBotAdmin()

                val request = mapOf(
                    "botName" to "Ciao", "roleList" to listOf<String>()
                ).toJson()

                handleRequest(HttpMethod.Put, "${Costant.Url.BASE_API_URL}/managment/bot/edit", bot) {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(request)
                }.apply {
                    assertEquals(HttpStatusCode.BadRequest, response.status())
                }
            }
        }

        @Test
        fun `Create Bot with roleList null, Result is BadRequest`() {
            withRealTestApplication({
                installModuleToTest()
            }) {
                val bot = getMockBotAdmin()

                val request = mapOf(
                    "botName" to "Ciao", "roleList" to null
                ).toJson()

                handleRequest(HttpMethod.Put, "${Costant.Url.BASE_API_URL}/managment/bot/edit", bot) {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(request)
                }.apply {
                    assertEquals(HttpStatusCode.BadRequest, response.status())
                }
            }
        }

        @Test
        fun `Create Bot with role not exist, Result is BadRequest`() {
            withRealTestApplication({
                installModuleToTest()
            }) {
                val bot = getMockBotAdmin()

                val request = mapOf(
                    "botName" to "", "roleList" to listOf("ABC")
                ).toJson()

                handleRequest(HttpMethod.Put, "${Costant.Url.BASE_API_URL}/managment/bot/edit", bot) {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(request)
                }.apply {
                    assertEquals(HttpStatusCode.BadRequest, response.status())
                }
            }
        }
    }

    @Nested
    inner class BotDeleteTest {
        @Test
        fun `Delete Bot, Result is OK deleted`() {
            withRealTestApplication({
                installModuleToTest()
            }) {
                val bot = getBotMock()
                val botAdmin = getMockBotAdmin()

                handleRequest(HttpMethod.Delete, "${Costant.Url.BASE_API_URL}/managment/bot/${bot.id}", botAdmin) {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                }.apply {
                    assertEquals(HttpStatusCode.OK, response.status())

                    transaction {
                        assertTrue {
                            BotsRoles.select {
                                (BotsRoles.bot eq bot.id)
                            }.toList().isEmpty()
                        }

                        assertTrue {
                            Bots.select {
                                Bots.id eq bot.id
                            }.toList().isEmpty()
                        }
                    }
                }
            }
        }

        @Test
        fun `Delete Bot with bot id empty, Result is BadRequest`() {
            withRealTestApplication({
                installModuleToTest()
            }) {
                val botAdmin = getMockBotAdmin()

                handleRequest(HttpMethod.Delete, "${Costant.Url.BASE_API_URL}/managment/bot/", botAdmin) {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                }.apply {
                    assertEquals(HttpStatusCode.BadRequest, response.status())
                }
            }
        }

        @Test
        fun `Delete Bot with bot id empty, Result is NotFound`() {
            withRealTestApplication({
                installModuleToTest()
            }) {
                val botAdmin = getMockBotAdmin()

                handleRequest(
                    HttpMethod.Delete, "${Costant.Url.BASE_API_URL}/managment/bot/${UUID.randomUUID()}", botAdmin
                ) {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                }.apply {
                    assertEquals(HttpStatusCode.NotFound, response.status())
                }
            }
        }
    }
}
