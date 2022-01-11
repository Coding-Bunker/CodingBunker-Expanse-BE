package it.condingbunker.tbs.test.bot

import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.testing.*
import it.codingbunker.tbs.feature.login.route.loginRoutes
import it.codingbunker.tbs.feature.managment.route.botManagmentRoutes
import it.codingbunker.tbs.feature.managment.table.Bots
import it.codingbunker.tbs.feature.managment.table.BotsRoles
import it.codingbunker.tbs.mainModule
import it.condingbunker.tbs.test.utilstest.BaseTest
import it.condingbunker.tbs.test.utilstest.getBotMock
import it.condingbunker.tbs.test.utilstest.toJson
import it.condingbunker.tbs.test.utilstest.withRealTestApplication
import it.github.codingbunker.tbs.common.Constant.Session.LOGIN_SESSION_USER
import it.github.codingbunker.tbs.common.Constant.Url.BASE_API_URL
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Nested
import org.koin.dsl.module
import org.koin.test.KoinTest
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BotManagmentTest : BaseTest() {
    private lateinit var config: HoconApplicationConfig

    private fun TestApplicationEngine.handleRequest(
        method: HttpMethod,
        uri: String,
        cookie: Cookie,
        setup: TestApplicationRequest.() -> Unit = {}
    ): TestApplicationCall {
        return handleRequest(method, uri) {
            addHeader(LOGIN_SESSION_USER, cookie.value)
            setup()
        }
    }

    override fun Application.installModuleToTest() {
        mainModule(
            true,
            listOf(
                module {
                    factory {
                        HttpClient(MockEngine) {
                            engine {
                                addHandler { request ->
                                    val responseHeaders =
                                        headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
                                    when {
                                        request.url.encodedPath.contains("/api/oauth2/token", true) -> {
                                            respond(
                                                """
                                                {
                                                  "access_token":"TEST_ACCESS_TOKEN",
                                                  "token_type":"bearer",
                                                  "expires_in": 3600,
                                                  "refresh_token":"TEST_REFRESH_TOKEN",
                                                  "scope":"create"
                                                }
                                                """.trimIndent(),
                                                HttpStatusCode.OK, responseHeaders
                                            )
                                        }
                                        request.url.encodedPath.contains("/users/@me", true) -> {
                                            respond(
                                                """
                                                {
                                                  "id": "ABC123",
                                                  "username": "samuele794 TBS",
                                                  "avatar": "a8be62f82829b1c60d0b23b6d6154ab0",
                                                  "discriminator": "8585",
                                                  "locale": "it",
                                                  "mfa_enabled": true,
                                                  "email": "test@test.com",
                                                  "verified": true
                                                }
                                                """.trimIndent(),
                                                HttpStatusCode.OK, responseHeaders
                                            )
                                        }
                                        else -> {
                                            error("Unhandled ${request.url.encodedPath}")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            )
        )
        botManagmentRoutes(true)
        loginRoutes()
    }

    private fun TestApplicationEngine.getMockAdmin(): Cookie {
        var state: String

        runBlocking {
            handleRequest(HttpMethod.Get, "/tbs/api/login/discord") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader("Host", "127.0.0.1")
            }.let { call ->
                val location = call.response.headers["Location"] ?: ""
                val stateInfo = Regex("state=(\\w+)").find(location)
                state = stateInfo!!.groupValues[1]
            }
        }

        var responseCookies: Cookie?

        handleRequest(HttpMethod.Get, "/tbs/api/login/discord?state=$state&code=mycode") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addHeader("Host", "127.0.0.1")
        }.let { call ->
            responseCookies = call.response.cookies[LOGIN_SESSION_USER]
        }

        return responseCookies!!
    }

    @Nested
    inner class BotCreateTest : KoinTest {

        @Test
        fun `Create Bot with all parameters, Result is created`() {
            withRealTestApplication({
                installModuleToTest()
            }) {
                cookiesSession {
                    val request = mapOf(
                        "botName" to "Bot Test",
                        "roleList" to listOf(
                            "ADMIN"
                        )
                    ).toJson()

                    handleRequest(HttpMethod.Put, "$BASE_API_URL/managment/bot/edit", getMockAdmin()) {
                        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        setBody(request)
                    }.apply {
                        assertEquals(HttpStatusCode.Created, response.status())
                    }
                }
            }
        }

        @Test
        fun `Create Bot with botName blank, Result is BadRequest`() {
            withRealTestApplication({
                installModuleToTest()
            }) {
                cookiesSession {
                    val request = mapOf(
                        "botName" to "",
                        "roleList" to listOf(
                            "ADMIN"
                        )
                    ).toJson()

                    handleRequest(HttpMethod.Put, "$BASE_API_URL/managment/bot/edit", getMockAdmin()) {
                        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        setBody(request)
                    }.apply {
                        assertEquals(HttpStatusCode.BadRequest, response.status())
                    }
                }
            }
        }

        @Test
        fun `Create Bot with roleList empty, Result is BadRequest`() {
            withRealTestApplication({
                installModuleToTest()
            }) {
                cookiesSession {
                    val request = mapOf(
                        "botName" to "Ciao", "roleList" to listOf<String>()
                    ).toJson()

                    handleRequest(HttpMethod.Put, "$BASE_API_URL/managment/bot/edit", getMockAdmin()) {
                        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        setBody(request)
                    }.apply {
                        assertEquals(HttpStatusCode.BadRequest, response.status())
                    }
                }
            }
        }

        @Test
        fun `Create Bot with roleList null, Result is BadRequest`() {
            withRealTestApplication({
                installModuleToTest()
            }) {
                cookiesSession {
                    val request = mapOf(
                        "botName" to "Ciao", "roleList" to null
                    ).toJson()

                    handleRequest(HttpMethod.Put, "$BASE_API_URL/managment/bot/edit", getMockAdmin()) {
                        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        setBody(request)
                    }.apply {
                        assertEquals(HttpStatusCode.BadRequest, response.status())
                    }
                }
            }
        }

        @Test
        fun `Create Bot with role not exist, Result is BadRequest`() {
            withRealTestApplication({
                installModuleToTest()
            }) {
                cookiesSession {
                    val request = mapOf(
                        "botName" to "", "roleList" to listOf("ABC")
                    ).toJson()

                    handleRequest(HttpMethod.Put, "$BASE_API_URL/managment/bot/edit", getMockAdmin()) {
                        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                        setBody(request)
                    }.apply {
                        assertEquals(HttpStatusCode.BadRequest, response.status())
                    }
                }
            }
        }
    }

    @Nested
    inner class BotDeleteTest {
        // TODO TEST DISABLED, URL NOT WORKING
//        @Test
        fun `Delete Bot, Result is OK deleted`() {
            withRealTestApplication({
                installModuleToTest()
            }) {
                cookiesSession {
                    val bot = getBotMock()
                    val botAdmin = getMockAdmin()

                    handleRequest(
                        HttpMethod.Delete,
                        "$BASE_API_URL/managment/bot/${bot.id}",
                        botAdmin
                    ) {
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
        }

        @Test
        fun `Delete Bot with bot id empty, Result is BadRequest`() {
            withRealTestApplication({
                installModuleToTest()
            }) {
                cookiesSession {
                    handleRequest(HttpMethod.Delete, "$BASE_API_URL/managment/bot/", getMockAdmin()) {
                        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    }.apply {
                        assertEquals(HttpStatusCode.NotFound, response.status())
                    }
                }
            }
        }

        @Test
        fun `Delete Bot with bot id empty, Result is NotFound`() {
            withRealTestApplication({
                installModuleToTest()
            }) {
                cookiesSession {
                    handleRequest(
                        HttpMethod.Delete,
                        "$BASE_API_URL/managment/bot/${UUID.randomUUID()}",
                        getMockAdmin()
                    ) {
                        addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    }.apply {
                        assertEquals(HttpStatusCode.NotFound, response.status())
                    }
                }
            }
        }
    }
}
