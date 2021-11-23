package it.codingbunker.tbs.feature.managment.table

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import java.util.*

object Bots : UUIDTable() {
    val botName = varchar("botName", 50)
    val botKey = varchar("botKey", 250)
    val botDateCreation = timestamp("botCreationDateTime").default(Instant.now())
}

class Bot(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Bot>(Bots)

    var botName: String by Bots.botName
    var botKey: String by Bots.botKey
    var botDateCreation: Instant by Bots.botDateCreation
    var botRoles by Role via BotsRoles
}

object BotsRoles : Table() {
    val bot = reference("bot", Bots, onDelete = ReferenceOption.CASCADE)
    val botRoles = reference("bot_roles", Roles)
    override val primaryKey = PrimaryKey(bot, botRoles)
}

class BotDTO(
    var id: String,
    var botName: String,
    var botKey: String,
    var botDateCreation: Instant,
    var botRoles: Set<RoleType>
)
