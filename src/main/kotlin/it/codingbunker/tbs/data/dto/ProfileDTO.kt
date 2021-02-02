package it.codingbunker.tbs.data.dto

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.auth.jwt.*
import it.codingbunker.tbs.data.converter.JWTConverter
import it.codingbunker.tbs.data.table.RoleType
import it.codingbunker.tbs.utils.CryptoInterface

interface ProfileDTO {

	val id: String
	val profileType: ProfileType
	val role: Set<RoleType>

	enum class ProfileType(val converter: JWTConverter<JWTPrincipal, ProfileDTO>) {
		/*USER(object : JWTConverter<JWTPrincipal, BotJWTDTO>() {
			override fun convert(jwtSource: JWTPrincipal): BotJWTDTO {
				TODO("Not yet implemented")
			}
		}),*/
		BOT(object : JWTConverter<JWTPrincipal, BotJWTDTO>() {
			override fun convertCrypted(
				jwtSource: JWTPrincipal,
				cryptoInterface: CryptoInterface
			): BotJWTDTO {
				return BotJWTDTO(
					id = cryptoInterface.decrypt(jwtSource.payload.getClaim(ProfileDTO::id.name).asString()),
					profileType = BOT,
					role = jacksonObjectMapper().readValue(
						cryptoInterface.decrypt(jwtSource.payload.getClaim(ProfileDTO::role.name).asString()),
						object :
							TypeReference<List<RoleType>>() {}
					).toSet()
				)

			}
		})
	}
}