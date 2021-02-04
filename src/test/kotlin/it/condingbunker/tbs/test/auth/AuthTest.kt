package it.condingbunker.tbs.test.auth

import io.ktor.application.*
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.testing.*
import it.codingbunker.tbs.data.client.TakaoSQLClient
import it.codingbunker.tbs.data.dto.ProfileDTO
import it.codingbunker.tbs.data.dto.request.LoginRequest
import it.codingbunker.tbs.data.table.Bot
import it.codingbunker.tbs.data.table.Role
import it.codingbunker.tbs.data.table.RoleType
import it.codingbunker.tbs.mainModule
import it.codingbunker.tbs.service.authenticationRoutes
import it.codingbunker.tbs.utils.Costant
import it.codingbunker.tbs.utils.getPropertyString
import it.condingbunker.tbs.test.utilstest.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.test.KoinTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthTest : KoinTest {

	private lateinit var takaoSQLClient: TakaoSQLClient
	private lateinit var config: HoconApplicationConfig

	private fun Application.installAuthModules() {
		mainModule(true)
		authenticationRoutes(true)
	}

	private fun TestApplicationEngine.addMockBot(): String =
		transaction {
			val role = Role.new(RoleType.BOT) {
				this.roleName = RoleType.BOT
			}

			commit()

			return@transaction Bot.new {
				this.botName = "Bot Test"
				this.permissions = SizedCollection(listOf(role))
			}.id.value.toString()
		}


	@BeforeTest
	fun `Clear DB For Test`() {
		if (::config.isInitialized.not()) {
			config = loadConfig()
		}

		if (::takaoSQLClient.isInitialized.not()) {
			takaoSQLClient = TakaoSQLClient(
				serverAddress = config.getPropertyString(Costant.Database.ADDRESS_DB_KEY),
				usernameDB = config.getPropertyString(Costant.Database.USERNAME_DB_KEY),
				passwordDB = config.getPropertyString(Costant.Database.PASSWORD_DB_KEY),
				driverDB = config.getPropertyString(Costant.Database.DRIVER_DB_KEY)
			)
		}

		runBlocking {
			transaction {
				//TODO Sistemare Funzione per adeguamento in base al dialetto
				TransactionManager.current().exec("DROP ALL OBJECTS")
			}
		}
	}

	@Test
	fun `Test Auth Bot exist, Result Success`() {
		withRealTestApplication({
			installAuthModules()
		}) {
			val botId = addMockBot()

			handleRequest(HttpMethod.Post, "${Costant.Url.BASE_API_URL}/auth/login") {
				addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
				setBody(
					LoginRequest(botId, ProfileDTO.ProfileType.BOT.name).toJson()
				)
			}.apply {
				assertEquals(response.status(), HttpStatusCode.OK)
				assert(this.response.content.isNullOrEmpty().not())
			}
		}
	}

	@Test
	fun `Test Auth Bot Not Exist, Result Success`() {
		withRealTestApplication({
			installAuthModules()
		}) {
			handleRequest(HttpMethod.Post, "${Costant.Url.BASE_API_URL}/auth/login") {
				addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
				setBody(
					LoginRequest("abc1234", ProfileDTO.ProfileType.BOT.name).toJson()
				)
			}.apply {
				assertEquals(response.status(), HttpStatusCode.Unauthorized)
			}
		}
	}
}