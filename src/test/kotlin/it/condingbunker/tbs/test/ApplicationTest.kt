package it.condingbunker.tbs.test

import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.testing.*
import it.codingbunker.tbs.data.client.TakaoMongoClient
import it.codingbunker.tbs.data.repo.DiscordRepositoryInterface
import it.codingbunker.tbs.mainModule
import it.codingbunker.tbs.service.discordGuildRoutes
import it.codingbunker.tbs.utils.Costant
import it.condingbunker.tbs.test.data.client.TakaoMongoClientTest
import it.condingbunker.tbs.test.utilstest.installMockKoin
import it.condingbunker.tbs.test.utilstest.withRealTestApplication
import org.koin.ktor.ext.inject
import org.koin.test.KoinTest
import org.koin.test.get
import org.litote.kmongo.id.StringId
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest : KoinTest {
    @Test
    fun `Create Guild with all parameters`() {

        val serverId = "AZ1123"

        withRealTestApplication(
            {
                mainModule(true)
                discordGuildRoutes(true)
                installMockKoin()
            }
        ) {
            val mongoClient: TakaoMongoClientTest = get<TakaoMongoClient>() as TakaoMongoClientTest
            mongoClient.dropDatabase()

            handleRequest(HttpMethod.Put, "${Costant.BASE_API_URL}/discord/guild/create") {
                addHeader("Content-type", "application/json")
                setBody(
                    Gson().toJson(
                        mapOf(
                            "serverId" to serverId,
                            "symbolCommand" to "!",
                            "guildName" to "codingBunky"
                        )
                    )
                )
            }.apply {
                assertEquals(HttpStatusCode.Created, response.status())

                val discordRepositoryInterface by inject<DiscordRepositoryInterface>()

                val result = discordRepositoryInterface.existDiscordGuildById(StringId(serverId))

                assertEquals(true, result)
            }
        }
    }


}
