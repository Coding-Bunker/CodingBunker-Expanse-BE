package it.codingbunker.tbs.data.bean.guild

import io.ktor.locations.*
import it.codingbunker.tbs.data.bean.StringEntityClass
import it.codingbunker.tbs.data.bean.StringIdTable
import it.codingbunker.tbs.utils.Costant.BASE_API_URL
import org.apache.commons.text.StringEscapeUtils
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.id.EntityID

@Location("$BASE_API_URL/discord/guild/")
class DiscordGuildRoute {
    @Location("/{serverId}")
    class DiscordGuildRouteGet(val parent: DiscordGuildRoute, var serverId: String)
}


object DiscordGuilds : StringIdTable(
    columnName = "guildId",
    columnLength = 100
) {
    val guildName = varchar("guildName", 255)
    val symbolCommand = varchar("symbolCommand", 25).default("!")
}

class DiscordGuild(id: EntityID<String>) : Entity<String>(id) {
    companion object : StringEntityClass<DiscordGuild>(DiscordGuilds)

    var guildName by DiscordGuilds.guildName
    var symbolCommand by DiscordGuilds.symbolCommand.transform(
        { StringEscapeUtils.escapeJava(it) },
        { StringEscapeUtils.unescapeJava(it) })
}

class DiscordGuildDTO(
    var guildId: String,
    var guildName: String,
    var symbolCommand: String = "%"
)