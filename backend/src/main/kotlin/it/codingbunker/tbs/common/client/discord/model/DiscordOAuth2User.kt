package it.codingbunker.tbs.common.client.discord.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiscordOAuth2User(
    @SerialName("id")
    val id: String,
    @SerialName("username")
    val username: String,
    @SerialName("avatar")
    val avatar: String,
    @SerialName("discriminator")
    val discriminator: String,
    @SerialName("locale")
    val locale: String,
    @SerialName("mfa_enabled")
    val mfaEnabled: Boolean,
    @SerialName("email")
    val email: String,
    @SerialName("verified")
    val verified: Boolean
)
