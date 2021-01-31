package it.codingbunker.tbs.data.table

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

enum class RoleType {
	ADMIN,
	BOT
}

object Roles : IdTable<RoleType>() {
	val roleName = enumerationByName("role_name", 200, RoleType::class)
	override val id: Column<EntityID<RoleType>> = enumeration("role_id", RoleType::class).entityId()
}

class Role(id: EntityID<RoleType>) : Entity<RoleType>(id) {
	companion object : EntityClass<RoleType, Role>(Roles)

	var permissionName by Roles.roleName
}