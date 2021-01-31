package it.codingbunker.tbs.data.table

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.timestamp
import java.util.*

object Bots : UUIDTable() {
	val botName = varchar("botName", 50)
	val dateCreation = timestamp("botCreationDateTime")
}

class Bot(id: EntityID<UUID>) : UUIDEntity(id) {
	companion object : UUIDEntityClass<Bot>(Bots)

	var botName: String by Bots.botName
	var permissions by Role via BotsPermissions
}

object BotsPermissions : Table() {
	val bot = reference("bot", Bots)
	val permission = reference("permission", Roles)
	override val primaryKey = PrimaryKey(bot, permission)
}