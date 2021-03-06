package it.codingbunker.tbs.data.repo

import com.github.kittinunf.result.coroutines.SuspendableResult
import it.codingbunker.tbs.data.table.Bot
import it.codingbunker.tbs.data.table.BotDTO
import it.codingbunker.tbs.data.table.Bots
import it.codingbunker.tbs.data.table.Role
import it.codingbunker.tbs.utils.onFailure
import it.codingbunker.tbs.utils.sha256Base64
import kotlinx.datetime.*
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.Slf4jSqlDebugLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID as UID

interface BotRepository {
	suspend fun existBotById(id: String): Boolean

	suspend fun generateBot(botName: String, roleList: Set<Role>): Bot

	suspend fun findBotByIdAndKey(id: String, key: String): SuspendableResult<BotDTO, Exception>

	suspend fun deleteBotById(id: String): SuspendableResult<Boolean, Exception>
}

class BotRepositoryImpl() : BaseRepository(), BotRepository {

	override suspend fun existBotById(id: String): Boolean {
		return Bot.findById(UID.fromString(id)) != null
	}

	override suspend fun generateBot(botName: String, roleList: Set<Role>): Bot =
		newSuspendedTransaction {
			Bot.new(UID.randomUUID()) {
				this.botName = botName
				botDateCreation = Clock.System.now()
					.toLocalDateTime(TimeZone.UTC)
					.toInstant(TimeZone.UTC)
					.toJavaInstant()
				botRoles = SizedCollection(roleList)
				botKey = id.value.toString().sha256Base64().run {
					botName.sha256Base64(this)
				}
			}
		}


	override suspend fun findBotByIdAndKey(id: String, key: String): SuspendableResult<BotDTO, Exception> =
		newSuspendedTransaction {
			addLogger(Slf4jSqlDebugLogger)

			SuspendableResult.of {

				val result = Bot.find {
					(Bots.id eq UID.fromString(id)) and
							(Bots.botKey eq key)
				}

				if (result.empty()) {
					throw NoSuchElementException("Bot not found")
				} else {
					result.first().run {
						BotDTO(
							id = id,
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

	override suspend fun deleteBotById(id: String) = newSuspendedTransaction {
		SuspendableResult.of<Boolean, Exception> {
			Bot.findById(UID.fromString(id))?.delete() ?: throw Exception("Bot not found")
			true
		}.onFailure {
			rollback()
		}
	}
}