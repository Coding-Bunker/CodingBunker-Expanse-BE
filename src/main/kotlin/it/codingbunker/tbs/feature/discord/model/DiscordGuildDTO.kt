package it.codingbunker.tbs.feature.discord.model

import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DiscordGuildDTO(
    @SerialName("guildId") @Required var guildId: String,
    @SerialName("guildName") @Required var guildName: String,
    @SerialName("symbolCommand") var symbolCommand: String = "%"
)
