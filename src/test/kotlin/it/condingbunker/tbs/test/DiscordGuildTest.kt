package it.condingbunker.tbs.test

import io.ktor.application.*
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.testing.*
import it.codingbunker.tbs.data.client.TakaoSQLClient
import it.codingbunker.tbs.data.dto.DiscordGuildDTO
import it.codingbunker.tbs.data.repo.DiscordRepositoryInterface
import it.codingbunker.tbs.data.table.DiscordGuild
import it.codingbunker.tbs.mainModule
import it.codingbunker.tbs.service.authenticationRoutes
import it.codingbunker.tbs.service.discordGuildRoutes
import it.codingbunker.tbs.utils.Costant.Database.ADDRESS_DB_KEY
import it.codingbunker.tbs.utils.Costant.Database.DRIVER_DB_KEY
import it.codingbunker.tbs.utils.Costant.Database.PASSWORD_DB_KEY
import it.codingbunker.tbs.utils.Costant.Database.USERNAME_DB_KEY
import it.codingbunker.tbs.utils.Costant.Url.BASE_API_URL
import it.codingbunker.tbs.utils.getPropertyString
import it.condingbunker.tbs.test.utilstest.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Nested
import org.koin.ktor.ext.inject
import org.koin.test.KoinTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class DiscordGuildTest : KoinTest {
    private lateinit var takaoSQLClient: TakaoSQLClient
    private lateinit var config: HoconApplicationConfig


    @BeforeTest
    fun `Clear DB For Test`() {
        if (::config.isInitialized.not()) {
            config = loadConfig()
        }

        if (::takaoSQLClient.isInitialized.not()) {
            takaoSQLClient = TakaoSQLClient(
				serverAddress = config.getPropertyString(ADDRESS_DB_KEY),
				usernameDB = config.getPropertyString(USERNAME_DB_KEY),
				passwordDB = config.getPropertyString(PASSWORD_DB_KEY),
				driverDB = config.getPropertyString(DRIVER_DB_KEY)
			)
        }

        runBlocking {
            transaction {
                //TODO Sistemare Funzione per adeguamento in base al dialetto
                TransactionManager.current().exec("DROP ALL OBJECTS")
            }
        }
    }

    @Nested
    inner class GuildCreationTest {

        private fun Application.installDiscordGuildModules() {
            mainModule(true)
            authenticationRoutes(true)
            discordGuildRoutes(true)
        }

        private fun insertMockGuild(serverId: String) {
            val requestInsert = DiscordGuildDTO(
                guildId = serverId,
                symbolCommand = "!",
                guildName = "codingBunky"
            )

            transaction {
                DiscordGuild.new(requestInsert.guildId) {
                    guildName = requestInsert.guildName
                    symbolCommand = requestInsert.symbolCommand
                }
            }
        }

        @Test
        fun `Create Guild with all parameters, Result is created`() {
            val serverId = "AZ1123"

            val request = DiscordGuildDTO(
                guildId = serverId,
                symbolCommand = "😉",
                guildName = "codingBunky"
            )

            withRealTestApplication(
                {
                    installDiscordGuildModules()
                }
            ) {
                val jwt = getBotJWT()

                handleRequest(HttpMethod.Put, "${BASE_API_URL}/discord/guild", jwt) {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(request.toJson())
                }.apply {
                    assertEquals(HttpStatusCode.Created, response.status())

                    runBlocking {
                        val discordRepositoryInterface by inject<DiscordRepositoryInterface>()
                        val result = discordRepositoryInterface.existDiscordGuildById(serverId)
                        assertEquals(true, result)
                    }
                }
            }
        }

        @Test
        fun `Create Guild it exist, Result is conflict`() {
            val serverId = "AZ1123"

            val request = DiscordGuildDTO(
                guildId = serverId,
                symbolCommand = "!",
                guildName = "codingBunky"
            )

            withRealTestApplication({
                installDiscordGuildModules()
            }) {
                insertMockGuild(serverId)

                val jwt = getBotJWT()

                handleRequest(HttpMethod.Put, "${BASE_API_URL}/discord/guild", jwt) {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(request.toJson())
                }.apply {
                    assertEquals(HttpStatusCode.Conflict, response.status())

                    runBlocking {
                        val discordRepositoryInterface by inject<DiscordRepositoryInterface>()
                        val result = discordRepositoryInterface.existDiscordGuildById(serverId)
                        assertEquals(true, result)
                    }
                }
            }
        }

        @Test
        fun `Create guild server id blank, Result is BadRequest`() {
            val request = DiscordGuildDTO(
                guildId = "",
                symbolCommand = "!",
                guildName = "codingBunky"
            )


            withRealTestApplication({
                installDiscordGuildModules()
            }) {
                val jwt = getBotJWT()

                handleRequest(HttpMethod.Put, "${BASE_API_URL}/discord/guild", jwt) {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(request.toJson())
                }.apply {
                    assertEquals(HttpStatusCode.BadRequest, response.status())
                }
            }
        }

        @Test
        fun `Create guild no server id, Result is BadRequest`() {
            val request = mapOf(
                "symbolCommand" to "!",
                "guildName" to "codingBunky"
            ).toJson()


            withRealTestApplication({
                installDiscordGuildModules()
            }) {
                val jwt = getBotJWT()

                handleRequest(HttpMethod.Put, "${BASE_API_URL}/discord/guild", jwt) {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(request)
                }.apply {
                    assertEquals(HttpStatusCode.BadRequest, response.status())
                }
            }
        }

        @Test
        fun `Create guild no name id, Result is BadRequest`() {
            val serverId = "AZ1123"

            val request = mapOf(
                "serverId" to serverId,
                "symbolCommand" to "!"
            ).toJson()


            withRealTestApplication({
                installDiscordGuildModules()
            }) {
                val jwt = getBotJWT()

                handleRequest(HttpMethod.Put, "${BASE_API_URL}/discord/guild", jwt) {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(request)
                }.apply {
                    assertEquals(HttpStatusCode.BadRequest, response.status())
                }
            }
        }

        @Test
        fun `Get guild, Result is guild obj`() {
            val serverId = "AZ1123"

            val responseJSON = DiscordGuildDTO(
                guildId = serverId,
                symbolCommand = "!",
                guildName = "codingBunky"
            ).toJson()

            withRealTestApplication({
                installDiscordGuildModules()
            }) {
                insertMockGuild(serverId)

                val jwt = getBotJWT()

                handleRequest(HttpMethod.Get, "${BASE_API_URL}/discord/guild/$serverId", jwt) {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                }.apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                    assertEquals(
                        responseJSON,
                        response.content.toString().replace(Regex("\n"), "").replace(Regex("\\s+"), "")
                    )
                }
            }
        }

        @Test
        fun `Get guild, Result is 404`() {
            val serverId = "AZ1123"

            withRealTestApplication({
                installDiscordGuildModules()
            }) {
                val jwt = getBotJWT()

                handleRequest(HttpMethod.Get, "${BASE_API_URL}/discord/guild/$serverId", jwt) {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                }.apply {
                    assertEquals(HttpStatusCode.NotFound, response.status())

                }
            }
        }

        @Test
        fun `Edit guild, Result is edited`() {
            val serverId = "AZ1123"

            val request = DiscordGuildDTO(
                guildId = serverId,
                symbolCommand = "%",
                guildName = "codingBunka"
            ).toJson()

            withRealTestApplication({
                installDiscordGuildModules()
            }) {
                insertMockGuild(serverId)

                val jwt = getBotJWT()

                handleRequest(HttpMethod.Patch, "${BASE_API_URL}/discord/guild/$serverId", jwt) {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(request)
                }.apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                    assertEquals(
                        request,
                        response.content.toString().replace(Regex("\n"), "").replace(Regex("\\s+"), "")
                    )
                }
            }
        }

        @Test
        fun `Edit guild with symbolCommand null, Result edited`() {
            val serverId = "AZ1123"

            val request = mapOf(
                DiscordGuildDTO::guildId.name to serverId,
                DiscordGuildDTO::guildName.name to "codingBunka"
            ).toJson()

            withRealTestApplication({
                installDiscordGuildModules()
            }) {
                insertMockGuild(serverId)

                val jwt = getBotJWT()

                handleRequest(HttpMethod.Patch, "${BASE_API_URL}/discord/guild/$serverId", jwt) {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(request)
                }.apply {
                    assertEquals(HttpStatusCode.OK, response.status())
                    assertEquals(
                        DiscordGuildDTO(
                            guildId = serverId,
                            symbolCommand = "%",
                            guildName = "codingBunka"
                        ).toJson(),
                        response.content.toString().replace(Regex("\n"), "").replace(Regex("\\s+"), "")
                    )
                }
            }
        }

        @Test
        fun `Edit guild with guildId empty, Result error`() {
            val serverId = "AZ1123"

            val request = DiscordGuildDTO(
                guildId = "",
                symbolCommand = "%",
                guildName = "codingBunka"
            ).toJson()

            withRealTestApplication({
                installDiscordGuildModules()
            }) {
                insertMockGuild(serverId)

                val jwt = getBotJWT()

                handleRequest(HttpMethod.Patch, "${BASE_API_URL}/discord/guild/$serverId", jwt) {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(request)
                }.apply {
                    assertEquals(HttpStatusCode.InternalServerError, response.status())
                }
            }
        }

        @Test
        fun `Delete guild, Result is deleted`() {
            val serverId = "AZ1123"

            val request = DiscordGuildDTO(
                guildId = serverId,
                symbolCommand = "%",
                guildName = "codingBunka"
            ).toJson()

            withRealTestApplication({
                installDiscordGuildModules()
            }) {
                insertMockGuild(serverId)

                val jwt = getBotJWT()

                handleRequest(HttpMethod.Delete, "${BASE_API_URL}/discord/guild/$serverId", jwt) {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(request)
                }.apply {
                    assertEquals(HttpStatusCode.OK, response.status())

                    val discordRepo by inject<DiscordRepositoryInterface>()
                    runBlocking {
                        assertFalse(discordRepo.existDiscordGuildById(serverId))
                    }
                }
            }
        }

        @Test
        fun `Delete guild not exist, Result is error`() {
            val serverId = "AZ1123"

            val request = DiscordGuildDTO(
                guildId = serverId,
                symbolCommand = "%",
                guildName = "codingBunka"
            ).toJson()

            withRealTestApplication({
                installDiscordGuildModules()
            }) {
                val jwt = getBotJWT()

                handleRequest(HttpMethod.Delete, "${BASE_API_URL}/discord/guild/$serverId", jwt) {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(request)
                }.apply {
                    assertEquals(HttpStatusCode.NotFound, response.status())
                }
            }
        }

        @Test
        fun `Delete guild empty serverID, Result is error`() {
            val serverId = "AZ1123"

            val request = DiscordGuildDTO(
                guildId = serverId,
                symbolCommand = "%",
                guildName = "codingBunka"
            ).toJson()

            withRealTestApplication({
                installDiscordGuildModules()
            }) {
                insertMockGuild(serverId)

                val jwt = getBotJWT()

                handleRequest(HttpMethod.Delete, "${BASE_API_URL}/discord/guild", jwt) {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    setBody(request)
                }.apply {
                    assertEquals(HttpStatusCode.NotFound, response.status())
                }
            }
        }
    }
}
