package it.codingbunker.tbs.data.repo

import com.github.kittinunf.result.Result
import it.codingbunker.tbs.common.repository.BaseRepository
import it.codingbunker.tbs.data.table.DiscordGuild
import it.codingbunker.tbs.data.table.DiscordGuilds
import it.codingbunker.tbs.feature.discord.model.DiscordGuildDTO
import it.github.codingbunker.tbs.common.util.onFailure
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.sql.Connection.TRANSACTION_SERIALIZABLE

interface DiscordRepositoryInterface {
    suspend fun createDiscordGuild(discordGuild: DiscordGuildDTO)

    suspend fun updateDiscordGuild(discordGuild: DiscordGuildDTO): Result<DiscordGuildDTO, Exception>

    suspend fun existDiscordGuildById(guildId: String): Boolean

    suspend fun getDiscordGuildById(guildId: String): DiscordGuildDTO?

    suspend fun deleteDiscordGuildById(guildId: String): Result<Int, Exception>
}

class DiscordRepository : BaseRepository(), DiscordRepositoryInterface {

    override suspend fun createDiscordGuild(discordGuild: DiscordGuildDTO) {
        newSuspendedTransaction {
            addLogger(Slf4jSqlDebugLogger)

            if (existDiscordGuildById(discordGuild.guildId)) {
                return@newSuspendedTransaction
            } else {
                DiscordGuild.new(discordGuild.guildId) {
                    guildName = discordGuild.guildName
                    symbolCommand = discordGuild.symbolCommand
                }
            }
        }
    }

    override suspend fun updateDiscordGuild(discordGuild: DiscordGuildDTO): Result<DiscordGuildDTO, Exception> =
        newSuspendedTransaction {
            DiscordGuilds.update({ DiscordGuilds.id eq discordGuild.guildId }) {
                it[guildName] = discordGuild.guildName
                it[symbolCommand] = discordGuild.symbolCommand
            }

            commit()

            return@newSuspendedTransaction Result.of<DiscordGuildDTO, Exception> {
                newSuspendedTransaction(
                    context = Dispatchers.IO, transactionIsolation = TRANSACTION_SERIALIZABLE
                ) {
                    getDiscordGuildById(discordGuild.guildId)
                        ?: throw Exception("Update Fail, DiscordGuild not found after update")
                }
            }.onFailure {
                rollback()
            }
        }

    override suspend fun existDiscordGuildById(guildId: String): Boolean = newSuspendedTransaction {
        addLogger(Slf4jSqlDebugLogger)

        DiscordGuilds.select {
            DiscordGuilds.id eq guildId
        }.firstOrNull().run {
            return@newSuspendedTransaction this != null
        }
    }

    override suspend fun getDiscordGuildById(guildId: String): DiscordGuildDTO? = newSuspendedTransaction {
        addLogger(Slf4jSqlDebugLogger)
        val result = DiscordGuild.findById(guildId) ?: return@newSuspendedTransaction null

        return@newSuspendedTransaction DiscordGuildDTO(
            guildId = result.id.value, guildName = result.guildName, symbolCommand = result.symbolCommand
        )
    }

    override suspend fun deleteDiscordGuildById(guildId: String): Result<Int, Exception> =
        newSuspendedTransaction {
            Result.of<Int, Exception> {
                newSuspendedTransaction {
                    addLogger(Slf4jSqlDebugLogger)

                    DiscordGuilds.deleteWhere { DiscordGuilds.id eq guildId }
                }
            }.onFailure {
                rollback()
                return@onFailure
            }
        }
}