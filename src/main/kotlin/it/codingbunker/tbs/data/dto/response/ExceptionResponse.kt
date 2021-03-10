package it.codingbunker.tbs.data.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ExceptionResponse(
	@JsonProperty("exception")
	val exception: String,
	@JsonProperty("cause")
	val cause: String
)