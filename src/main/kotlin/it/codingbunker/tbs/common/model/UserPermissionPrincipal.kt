package it.codingbunker.tbs.common.model

import it.codingbunker.tbs.data.table.RoleType

interface UserPermissionPrincipal {
    val roles: Set<RoleType>
}
