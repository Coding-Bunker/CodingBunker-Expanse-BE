package it.codingbunker.tbs.common.model

import it.github.codingbunker.tbs.common.model.RoleType

interface UserPermissionPrincipal {
    val roles: Set<RoleType>
}
