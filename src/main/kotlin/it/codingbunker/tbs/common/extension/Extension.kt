package it.codingbunker.tbs.common.extension

import io.ktor.config.*
import java.security.MessageDigest
import java.util.*

fun ApplicationConfig.getPropertyString(path: String) =
	this.property(path).getString()

fun String.sha256Base64(vararg text: String): String {
	val base64Encoder = Base64.getEncoder()

	return MessageDigest
		.getInstance("SHA-512").run {
			text.forEach {
				update(it.toByteArray())
			}
			update(toByteArray())
			base64Encoder.encodeToString(digest())
		}
}