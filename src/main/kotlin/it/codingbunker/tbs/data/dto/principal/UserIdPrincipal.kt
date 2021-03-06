package it.codingbunker.tbs.data.dto.principal

import io.ktor.auth.*

open class UserIdPrincipal(open val id: String) : Principal