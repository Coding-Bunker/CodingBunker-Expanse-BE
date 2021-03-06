package it.codingbunker.tbs.utils


import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplate
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.AesGcmKeyManager
import java.util.*
import kotlin.text.Charsets.UTF_8

interface CryptoClientInterface {
	fun encrypt(plainText: String): String

	fun decrypt(cypherText: String): String
}

class CryptoClientInterfaceImpl(private val aadSecret: String) : CryptoClientInterface {

	private var deterministicAead: Aead

	private val base64Encoder = Base64.getEncoder()
	private val base64Decoder = Base64.getDecoder()

	init {
		AeadConfig.register()
		val keysetTemplate: KeyTemplate = AesGcmKeyManager.rawAes256GcmTemplate()
		val keysetHandle: KeysetHandle = KeysetHandle.generateNew(keysetTemplate)

		deterministicAead = keysetHandle.getPrimitive(Aead::class.java)

	}

	override fun encrypt(plainText: String): String =
		base64Encoder.encodeToString(deterministicAead.encrypt(plainText.toByteArray(), aadSecret.toByteArray()))


	override fun decrypt(cypherText: String): String =
		deterministicAead.decrypt(base64Decoder.decode(cypherText), aadSecret.toByteArray()).toString(UTF_8)

}