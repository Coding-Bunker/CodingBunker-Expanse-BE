package it.codingbunker.tbs.feature.managment.model

import com.fasterxml.jackson.annotation.JsonProperty

class BotIdKeyDTO(
	@JsonProperty("botId", required = true)
	var botId: String,
	@JsonProperty("botKey", required = true)
	var botKey: String
)