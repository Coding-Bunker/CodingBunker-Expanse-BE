package it.codingbunker.tbs.data.repo

import it.codingbunker.tbs.data.bean.guild.DiscordGuild
import it.codingbunker.tbs.data.bean.guild.DiscordGuildDTO
import it.codingbunker.tbs.data.bean.guild.DiscordGuilds
import it.codingbunker.tbs.data.client.TakaoSQLClient
import org.jetbrains.exposed.sql.Slf4jSqlDebugLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.exposedLogger
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction


interface DiscordRepositoryInterface {
    suspend fun createDiscordGuild(discordGuild: DiscordGuildDTO)

    suspend fun existDiscordGuildById(guildId: String): Boolean

    suspend fun getDiscordGuildById(guildId: String): DiscordGuildDTO?
}

class DiscordRepository(private val takaoSQLClient: TakaoSQLClient) : DiscordRepositoryInterface {

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

    override suspend fun existDiscordGuildById(guildId: String): Boolean =
        newSuspendedTransaction {
            exposedLogger
            addLogger(Slf4jSqlDebugLogger)

            DiscordGuilds.select {
                DiscordGuilds.id eq guildId
            }.firstOrNull().run {
                return@newSuspendedTransaction this != null
            }
        }

    override suspend fun getDiscordGuildById(guildId: String): DiscordGuildDTO? =
        newSuspendedTransaction {
            val result = DiscordGuild.findById(guildId) ?: return@newSuspendedTransaction null

            return@newSuspendedTransaction DiscordGuildDTO(
                guildId = result.id.value,
                guildName = result.guildName,
                symbolCommand = result.symbolCommand
            )

        }

}