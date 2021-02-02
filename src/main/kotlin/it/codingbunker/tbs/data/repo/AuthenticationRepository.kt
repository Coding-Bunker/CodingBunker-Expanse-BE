package it.codingbunker.tbs.data.repo

import com.github.kittinunf.result.coroutines.SuspendableResult
import it.codingbunker.tbs.data.dto.BotJWTDTO
import it.codingbunker.tbs.data.dto.ProfileDTO
import it.codingbunker.tbs.data.table.Bot
import org.jetbrains.exposed.sql.Slf4jSqlDebugLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

interface AuthenticationInterface {
	suspend fun existBotById(id: String): Boolean

	suspend fun findBotById(id: String): SuspendableResult<BotJWTDTO, Exception>
}

class AuthenticationRepository : BaseRepository(), AuthenticationInterface {

	override suspend fun existBotById(id: String): Boolean {
		return Bot.findById(UUID.fromString(id)) != null
	}

	override suspend fun findBotById(id: String): SuspendableResult<BotJWTDTO, Exception> =
		newSuspendedTransaction {
			addLogger(Slf4jSqlDebugLogger)

			return@newSuspendedTransaction SuspendableResult.of {
				Bot[UUID.fromString(id)].run {
					BotJWTDTO(
						id = this.id.toString(),
						role = this.permissions.map {
							it.id.value
						}.toSet(),
						profileType = ProfileDTO.ProfileType.BOT
					)
				}
			}
		}

}