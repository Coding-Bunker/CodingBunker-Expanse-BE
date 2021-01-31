package it.codingbunker.tbs.data.dto

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.kittinunf.result.Result
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import it.codingbunker.tbs.data.converter.JWTConverter
import it.codingbunker.tbs.data.table.RoleType
import it.codingbunker.tbs.utils.CryptoInterface

interface ProfileDTO : Principal {

	val id: String
	val profileType: ProfileType
	val role: Set<RoleType>

	enum class ProfileType(converter: JWTConverter<JWTPrincipal, ProfileDTO>) {
		/*USER(object : JWTConverter<JWTPrincipal, BotJWTDTO>() {
			override fun convert(jwtSource: JWTPrincipal): BotJWTDTO {
				TODO("Not yet implemented")
			}
		}),*/
		BOT(object : JWTConverter<JWTPrincipal, BotJWTDTO>() {
			override fun convertCrypted(
				jwtSource: JWTPrincipal,
				cryptoInterface: CryptoInterface
			): Result<BotJWTDTO, Exception> {
				return Result.of {
					BotJWTDTO(
						id = cryptoInterface.decrypt(jwtSource.payload.getClaim(ProfileDTO::id.name).asString()),
						botName = jwtSource.payload.getClaim(BotJWTDTO::botName.name)
							.asString(),
						profileType = BOT,
						role = jacksonObjectMapper().readValue(
							cryptoInterface.decrypt(jwtSource.payload.getClaim(ProfileDTO::role.name).asString()),
							object :
								TypeReference<List<RoleType>>() {}
						).toSet()
					)
				}
			}
		})
	}
}