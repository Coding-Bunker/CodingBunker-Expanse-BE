package it.codingbunker.tbs.common.model

import io.ktor.auth.*

open class UserIdPrincipal(open val id: String) : Principal
