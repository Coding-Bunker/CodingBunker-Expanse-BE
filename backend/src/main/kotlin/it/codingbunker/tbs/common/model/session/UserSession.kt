package it.codingbunker.tbs.common.model.session

import io.ktor.auth.*
import it.codingbunker.tbs.common.model.UserPermissionPrincipal
import it.codingbunker.tbs.feature.managment.table.RoleType
import it.codingbunker.tbs.feature.managment.table.UserDTO
import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    var accessToken: String,
    val tokenType: String,
    val tokenExpireIn: Long,
    val userDTO: UserDTO
) : UserPermissionPrincipal, Principal {
    override val roles: Set<RoleType>
        get() = userDTO.userRoles
}
