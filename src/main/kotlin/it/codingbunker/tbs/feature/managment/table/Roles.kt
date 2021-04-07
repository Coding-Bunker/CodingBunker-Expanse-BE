package it.codingbunker.tbs.feature.managment.table

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.transactions.transaction

@Serializable
enum class RoleType {
    ADMIN, BOT_DISCORD, BOT_TWITCH, USER
}

object Roles : IdTable<RoleType>() {
    var roleName = enumerationByName("role_name", 200, RoleType::class)
    override var id: Column<EntityID<RoleType>> = enumeration("role_id", RoleType::class).entityId()
}

class Role(id: EntityID<RoleType>) : Entity<RoleType>(id) {
    companion object : EntityClass<RoleType, Role>(Roles) {

        fun initTableValue() {
            transaction {
                val missingPermission = RoleType.values().toList() - Role.all().map { it.id.value }

                missingPermission.forEach {
                    Role.new(it) {
                        roleName = it
                    }

                    commit()
                }
            }
        }
    }

    var roleName by Roles.roleName
}
