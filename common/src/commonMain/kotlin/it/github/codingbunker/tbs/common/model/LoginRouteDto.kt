package it.github.codingbunker.tbs.common.model

import kotlinx.serialization.Serializable

enum class LoginRoute(val route: String) {
    DISCORD("discord");

    companion object {
        fun getRoute(name: String): LoginRoute? {
            return values().find { it.route == name }
        }
    }
}

@Serializable
data class LoginRouteDto(val routeName: LoginRoute, val link: String)