package it.codingbunker.tbs.feature.managment.repository

import com.github.kittinunf.result.Result
import it.codingbunker.tbs.common.extension.sha256Base64
import it.codingbunker.tbs.common.repository.BaseRepository
import it.codingbunker.tbs.feature.managment.table.*
import it.github.codingbunker.tbs.common.util.onFailure
import kotlinx.datetime.*
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.Slf4jSqlDebugLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID as UID

interface BotRepository {
    suspend fun existBotById(botId: String): Boolean

    suspend fun generateBot(botName: String, roleList: Set<RoleType>): BotDTO

    suspend fun findBotById(botId: String): Result<BotDTO, Exception>

    suspend fun deleteBotById(botId: String): Result<Boolean, Exception>
    suspend fun getAllBots(): Result<List<BotDTO>, Exception>
}

class BotRepositoryImpl : BaseRepository(), BotRepository {

    override suspend fun existBotById(botId: String): Boolean {
        return newSuspendedTransaction {
            Bot.find {
                (Bots.id eq UID.fromString(botId))
            }.empty().not()
        }
    }

    override suspend fun generateBot(botName: String, roleList: Set<RoleType>): BotDTO = newSuspendedTransaction {

        Bot.new(UID.randomUUID()) {
            this.botName = botName
            botKey = id.value.toString().sha256Base64().run {
                botName.sha256Base64(this)
            }
            botDateCreation = Clock.System.now().toLocalDateTime(TimeZone.UTC).toInstant(TimeZone.UTC).toJavaInstant()
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

    override suspend fun findBotById(botId: String): Result<BotDTO, Exception> = newSuspendedTransaction {
        addLogger(Slf4jSqlDebugLogger)

        Result.of {
            val result = Bot.find {
                Bots.id eq UID.fromString(botId)
            }

            if (result.empty()) {
                throw NoSuchElementException("Bot not found")
            } else {
                result.first().convertToDTO()
            }
        }
    }

    override suspend fun getAllBots(): Result<List<BotDTO>, Exception> = newSuspendedTransaction {
        Result.of {
            Bot.all().map {
                it.convertToDTO()
            }
        }
    }

    override suspend fun deleteBotById(botId: String) = newSuspendedTransaction {
        Result.of<Boolean, Exception> {
            Bot.find {
                Bots.id eq UID.fromString(botId)
            }.first().delete()
            true
        }.onFailure {
            rollback()
        }
    }

    private fun Bot.convertToDTO(): BotDTO {
        return BotDTO(
            id = id.value.toString(),
            botKey = botKey,
            botName = botName,
            botDateCreation = botDateCreation,
            botRoles = botRoles.map {
                it.id.value
            }.toSet()
        )
    }
}
