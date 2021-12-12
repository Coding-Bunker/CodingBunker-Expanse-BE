package it.github.codingbunker.tbs.common.model

import kotlinx.serialization.Serializable


@Serializable
class BackendException(val code: ExceptionCode, val message: String)

enum class ExceptionCode(code: String) {
    LOGIN_ERROR("4001"),
    LOGIN_DISCORD_ERROR("4002")
}
