package it.codingbunker.tbs.data.converter

import io.ktor.auth.*
import it.codingbunker.tbs.data.dto.ProfileDTO
import it.codingbunker.tbs.utils.CryptoInterface

abstract class JWTConverter<in I : Principal, out O : ProfileDTO> {

	open fun convert(jwtSource: I): O? {
		return null
	}

	open fun convertCrypted(jwtSource: I, cryptoInterface: CryptoInterface): O? {
		return null
	}

}