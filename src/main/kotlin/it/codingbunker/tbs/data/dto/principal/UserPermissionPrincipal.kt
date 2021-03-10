package it.codingbunker.tbs.data.dto.principal

import it.codingbunker.tbs.data.table.RoleType

interface UserPermissionPrincipal {
	val roles: Set<RoleType>
}