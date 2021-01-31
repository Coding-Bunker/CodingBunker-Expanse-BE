package it.codingbunker.tbs.data.converter

import com.github.kittinunf.result.Result
import io.ktor.auth.*
import it.codingbunker.tbs.data.dto.ProfileDTO
import it.codingbunker.tbs.utils.CryptoInterface

abstract class JWTConverter<in I : Principal, out O : ProfileDTO> {

	open fun convert(jwtSource: I): Result<O, Exception>? {
		return null
	}

	open fun convertCrypted(jwtSource: I, cryptoInterface: CryptoInterface): Result<O, Exception>? {
		return null
	}

}