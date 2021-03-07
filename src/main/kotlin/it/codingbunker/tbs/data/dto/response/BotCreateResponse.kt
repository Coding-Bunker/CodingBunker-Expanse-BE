package it.codingbunker.tbs.data.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

class BotCreateResponse(
	@JsonProperty("botId", required = true)
	var botId: String,
	@JsonProperty("botKey", required = true)
	var botKey: String
)