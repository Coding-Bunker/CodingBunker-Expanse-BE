package it.condingbunker.tbs.test

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import it.codingbunker.tbs.data.bean.guild.DiscordGuild
import it.codingbunker.tbs.data.client.TakaoMongoClient
import it.codingbunker.tbs.data.repo.DiscordRepositoryInterface
import it.codingbunker.tbs.mainModule
import it.codingbunker.tbs.service.discordGuildRoutes
import it.codingbunker.tbs.utils.Costant
import it.codingbunker.tbs.utils.getPropertyString
import it.condingbunker.tbs.test.data.client.TakaoMongoClientTest
import it.condingbunker.tbs.test.utilstest.installMockKoin
import it.condingbunker.tbs.test.utilstest.loadConfig
import it.condingbunker.tbs.test.utilstest.toJson
import it.condingbunker.tbs.test.utilstest.withRealTestApplication
import org.junit.jupiter.api.Nested
import org.koin.ktor.ext.inject
import org.koin.test.KoinTest
import org.litote.kmongo.getCollection
import org.litote.kmongo.id.StringId
import org.litote.kmongo.insertOne
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest : KoinTest {
    private lateinit var takaoMongoClientTest: TakaoMongoClientTest


    @BeforeTest
    fun `Clear DB For Test`() {
        val config = loadConfig()

        takaoMongoClientTest = TakaoMongoClientTest(
            serverAddress = config.getPropertyString(TakaoMongoClient.ADDRESS_PROPERTY_KEY),
            databaseName = config.getPropertyString(TakaoMongoClient.DATABASE_NAME_PROPERTY_KEY)
        )

        takaoMongoClientTest.dropDatabase()
    }

    @Nested
    inner class GuildCreationTest {

        private fun Application.installDiscordGuildModules() {
            mainModule(true)
            discordGuildRoutes(true)
            installMockKoin()
        }

        @Test
        fun `Create Guild with all parameters, Result is created`() {
            val serverId = "AZ1123"

            val request = mapOf(
                "serverId" to serverId,
                "symbolCommand" to "!",
                "guildName" to "codingBunky"
            ).toJson()

            withRealTestApplication(
                {
                    installDiscordGuildModules()
                }
            ) {
                handleRequest(HttpMethod.Put, "${Costant.BASE_API_URL}/discord/guild") {
                    addHeader("Content-type", "application/json")
                    setBody(request)
                }.apply {
                    assertEquals(HttpStatusCode.Created, response.status())

                    val discordRepositoryInterface by inject<DiscordRepositoryInterface>()

                    val result = discordRepositoryInterface.existDiscordGuildById(StringId(serverId))

                    assertEquals(true, result)
                }
            }
        }

        @Test
        fun `Create Guild it exist, Result is conflict`() {
            val serverId = "AZ1123"

            val requestInsert = mapOf(
                "_id" to serverId,
                "symbolCommand" to "!",
                "guildName" to "codingBunky"
            ).toJson()

            val request = mapOf(
                "serverId" to serverId,
                "symbolCommand" to "!",
                "guildName" to "codingBunky"
            ).toJson()

            takaoMongoClientTest.mongoClientDB.getCollection<DiscordGuild>().run {
                insertOne(requestInsert)
            }

            withRealTestApplication({
                installDiscordGuildModules()
            }) {
                handleRequest(HttpMethod.Put, "${Costant.BASE_API_URL}/discord/guild") {
                    addHeader("Content-type", "application/json")
                    setBody(request)
                }.apply {
                    assertEquals(HttpStatusCode.Conflict, response.status())

                    val discordRepositoryInterface by inject<DiscordRepositoryInterface>()

                    val result = discordRepositoryInterface.existDiscordGuildById(StringId(serverId))

                    assertEquals(true, result)
                }
            }
        }

        @Test
        fun `Create guild server id blank, Result is BadRequest`() {
            val request = mapOf(
                "serverId" to "",
                "symbolCommand" to "!",
                "guildName" to "codingBunky"
            ).toJson()


            withRealTestApplication({
                installDiscordGuildModules()
            }) {
                handleRequest(HttpMethod.Put, "${Costant.BASE_API_URL}/discord/guild") {
                    addHeader("Content-type", "application/json")
                    setBody(request)
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
                handleRequest(HttpMethod.Put, "${Costant.BASE_API_URL}/discord/guild") {
                    addHeader("Content-type", "application/json")
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
                handleRequest(HttpMethod.Put, "${Costant.BASE_API_URL}/discord/guild") {
                    addHeader("Content-type", "application/json")
                    setBody(request)
                }.apply {
                    assertEquals(HttpStatusCode.BadRequest, response.status())
                }
            }
        }
    }


}
