package it.codingbunker.tbs.data.dto.response.bot

import com.fasterxml.jackson.annotation.JsonProperty

class BotIdKeyDTO(
	@JsonProperty("botId", required = true)
	var botId: String,
	@JsonProperty("botKey", required = true)
	var botKey: String
)