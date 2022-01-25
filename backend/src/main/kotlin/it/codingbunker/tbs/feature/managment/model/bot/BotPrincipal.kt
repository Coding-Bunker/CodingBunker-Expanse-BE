package it.codingbunker.tbs.feature.managment.model.bot

import it.codingbunker.tbs.common.model.UserIdPrincipal
import it.codingbunker.tbs.common.model.UserPermissionPrincipal
import it.github.codingbunker.tbs.common.model.RoleType

data class BotPrincipal(override val id: String, override val roles: Set<RoleType>) :
    UserIdPrincipal(id),
    UserPermissionPrincipal
