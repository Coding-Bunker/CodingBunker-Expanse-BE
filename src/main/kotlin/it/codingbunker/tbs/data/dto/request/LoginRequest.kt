package it.codingbunker.tbs.data.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

class LoginRequest(
	@JsonProperty("id", required = true)
	var id: String,
	@JsonProperty("accountType", required = true)
	val accountType: String
)