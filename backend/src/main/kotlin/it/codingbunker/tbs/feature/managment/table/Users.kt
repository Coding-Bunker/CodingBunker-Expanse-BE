package it.codingbunker.tbs.feature.managment.table

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import java.util.*

object Users : UUIDTable() {
    val discordId = varchar("discordId", 50)
    val avatarHash = varchar("avatarHash", 255).nullable()
    val avatarImageData = blob("avatarImageData").nullable()
    val username = varchar("username", 255)
    val discriminator = varchar("discriminator", 10).nullable()
    val email = varchar("email", 255)
    val mfaEnabled = bool("mfaEnabled").default(false)
    val dateCreation = timestamp("creationDateTime").default(Instant.now())
}

class User(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<User>(Users)

    var username by Users.username
    var discriminator by Users.discriminator
    var email by Users.email
    var mfaEnabled by Users.mfaEnabled
    var discordId by Users.discordId
    var avatarHash by Users.avatarHash
    var avatarImageData by Users.avatarImageData
    var dateCreation by Users.dateCreation
    var userRoles by Role via UsersRoles
}

object UsersRoles : Table() {
    val user = reference("user", Users, onDelete = ReferenceOption.CASCADE)
    val userRoles = reference("user_roles", Roles)
    override val primaryKey = PrimaryKey(user, userRoles)
}

@Serializable
class UserDTO(
    var id: String,
    var discordId: String,
    var username: String,
    var discriminator: String?,
    var email: String,
    var mfaEnabled: Boolean,
    var userRoles: Set<RoleType>,
    var avatarHash: String?,
    var avatarImageData: ByteArray?,
    val dateCreation: Long
) {
    constructor(
        id: String,
        discordId: String,
        username: String,
        discriminator: String?,
        email: String,
        mfaEnabled: Boolean,
        userRoles: Set<RoleType>,
        avatarHash: String?,
        avatarImageData: ByteArray?,
        dateCreation: Instant
    ) : this(
        id, discordId, username, discriminator, email, mfaEnabled, userRoles, avatarHash,
        avatarImageData, dateCreation.toEpochMilli()
    )
}