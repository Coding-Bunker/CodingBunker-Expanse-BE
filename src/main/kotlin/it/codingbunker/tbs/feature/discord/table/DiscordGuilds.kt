package it.codingbunker.tbs.data.table

import it.codingbunker.tbs.common.table.StringEntityClass
import it.codingbunker.tbs.common.table.StringIdTable
import org.apache.commons.text.StringEscapeUtils
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.id.EntityID

object DiscordGuilds : StringIdTable(
	columnName = "guildId",
	columnLength = 100
) {
	var guildName = varchar("guildName", 255)
	var symbolCommand = varchar("symbolCommand", 25).default("!")
}

class DiscordGuild(id: EntityID<String>) : Entity<String>(id) {
	companion object : StringEntityClass<DiscordGuild>(DiscordGuilds)

	var guildName: String by DiscordGuilds.guildName
	var symbolCommand: String by DiscordGuilds.symbolCommand.transform(
		{ StringEscapeUtils.escapeJava(it) },
		{ StringEscapeUtils.unescapeJava(it) })
}