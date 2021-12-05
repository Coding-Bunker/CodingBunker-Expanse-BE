package it.codingbunker.tbs.common.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExceptionResponse(
    @SerialName("exception") val exception: String,
    @SerialName("cause") val cause: String
)
