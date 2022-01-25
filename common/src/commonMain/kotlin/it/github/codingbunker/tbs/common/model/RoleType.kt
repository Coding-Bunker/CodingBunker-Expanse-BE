package it.github.codingbunker.tbs.common.model

import kotlinx.serialization.Serializable

@Serializable
enum class RoleType {
    ADMIN, BOT_DISCORD, BOT_TWITCH, USER
}