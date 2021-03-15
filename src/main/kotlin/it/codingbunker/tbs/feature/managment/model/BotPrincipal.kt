package it.codingbunker.tbs.feature.managment.model

import it.codingbunker.tbs.common.model.UserIdPrincipal
import it.codingbunker.tbs.common.model.UserPermissionPrincipal
import it.codingbunker.tbs.data.table.RoleType

data class BotPrincipal(override val id: String, override val roles: Set<RoleType>) : UserIdPrincipal(id),
	UserPermissionPrincipal