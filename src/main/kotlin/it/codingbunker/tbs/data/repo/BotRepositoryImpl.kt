package it.codingbunker.tbs.data.repo

import com.github.kittinunf.result.coroutines.SuspendableResult
import it.codingbunker.tbs.data.table.*
import it.codingbunker.tbs.utils.onFailure
import it.codingbunker.tbs.utils.sha256Base64
import kotlinx.datetime.*
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.Slf4jSqlDebugLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID as UID

interface BotRepository {
	suspend fun existBotById(botId: String): Boolean

	suspend fun generateBot(botName: String, roleList: Set<RoleType>): BotDTO

	suspend fun findBotById(botId: String): SuspendableResult<BotDTO, Exception>

	suspend fun deleteBotById(botId: String): SuspendableResult<Boolean, Exception>
}

class BotRepositoryImpl : BaseRepository(), BotRepository {

	override suspend fun existBotById(botId: String): Boolean {
		return newSuspendedTransaction {
			Bot.find {
				(Bots.id eq UID.fromString(botId))
			}.empty().not()
		}
	}

	override suspend fun generateBot(botName: String, roleList: Set<RoleType>): BotDTO =
		newSuspendedTransaction {

			Bot.new(UID.randomUUID()) {
				this.botName = botName
				botKey = id.value.toString().sha256Base64().run {
					botName.sha256Base64(this)
				}
				botDateCreation = Clock.System.now()
					.toLocalDateTime(TimeZone.UTC)
					.toInstant(TimeZone.UTC)
					.toJavaInstant()
				botRoles = SizedCollection(
					roleList.map {
						Role[it]
					}
				)
			}.run {
				BotDTO(
					id = this.id.value.toString(),
					botKey = botKey,
					botName = botName,
					botDateCreation = botDateCreation,
					botRoles = botRoles.map {
						it.id.value
					}.toSet()
				)
			}
		}


	override suspend fun findBotById(botId: String): SuspendableResult<BotDTO, Exception> =
		newSuspendedTransaction {
			addLogger(Slf4jSqlDebugLogger)

			SuspendableResult.of {

				val result = Bot.find {
					Bots.id eq UID.fromString(botId)
				}

				if (result.empty()) {
					throw NoSuchElementException("Bot not found")
				} else {
					result.first().run {
						BotDTO(
							id = this.id.value.toString(),
							botKey = botKey,
							botName = botName,
							botDateCreation = botDateCreation,
							botRoles = botRoles.map {
								it.id.value
							}.toSet()
						)
					}
				}

			}
		}

	override suspend fun deleteBotById(botId: String) = newSuspendedTransaction {
		SuspendableResult.of<Boolean, Exception> {
			Bot.find {
				Bots.id eq UID.fromString(botId)
			}.first().delete()
			true
		}.onFailure {
			rollback()
		}
	}
}