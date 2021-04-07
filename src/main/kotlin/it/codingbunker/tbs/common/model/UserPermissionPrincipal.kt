package it.codingbunker.tbs.common.model

import it.codingbunker.tbs.feature.managment.table.RoleType

interface UserPermissionPrincipal {
    val roles: Set<RoleType>
}
