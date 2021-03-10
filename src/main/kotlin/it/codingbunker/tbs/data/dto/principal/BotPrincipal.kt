package it.codingbunker.tbs.data.dto.principal

import it.codingbunker.tbs.data.table.RoleType

data class BotPrincipal(override val id: String, override val roles: Set<RoleType>) : UserIdPrincipal(id),
	UserPermissionPrincipal