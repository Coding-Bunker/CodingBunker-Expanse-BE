package it.codingbunker.tbs.feature.discord.model

import com.fasterxml.jackson.annotation.JsonProperty

class DiscordGuildDTO(
    @JsonProperty("guildId", required = true) var guildId: String,
    @JsonProperty("guildName", required = true) var guildName: String,
    @JsonProperty("symbolCommand", required = true, defaultValue = "%") var symbolCommand: String = "%"
)
