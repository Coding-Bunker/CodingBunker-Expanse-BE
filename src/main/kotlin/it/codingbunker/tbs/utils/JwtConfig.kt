package it.codingbunker.tbs.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.kittinunf.result.coroutines.SuspendableResult
import io.ktor.auth.jwt.*
import it.codingbunker.tbs.data.dto.BotJWTDTO
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.util.*
import kotlin.time.ExperimentalTime

class JwtConfig(
	private val issuer: String,
	private val subject: String,
	secretJWT: String,
	private val timeValid: Int,
	private val cryptoClient: CryptoInterface
) {
	private val algorithm = Algorithm.HMAC512(secretJWT)


	val jwtVerifier: JWTVerifier = JWT
		.require(algorithm)
		.withSubject(subject)
		.withIssuer(issuer)
		.build()

	/**
	 * Produce a token for this combination of User and Account
	 */
	suspend fun makeTokenBot(bot: BotJWTDTO): SuspendableResult<String, Exception> =
		SuspendableResult.of {
			JWT.create()
				.withSubject(subject)
				.withIssuer(issuer)
				.withClaim(bot::id.name, cryptoClient.encrypt(bot.id))
				.withClaim(bot::profileType.name, bot.profileType.ordinal)
				.withClaim(
					bot::role.name,
					cryptoClient.encrypt(jacksonObjectMapper().writeValueAsString(bot.role.toTypedArray()))
				)
				.withExpiresAt(getExpiration())
				.sign(algorithm)
		}

	fun convertJWTCredential(jwtCredential: JWTCredential): JWTPrincipal? {
		return if (jwtCredential.payload.claims.contains(BotJWTDTO::profileType.name))
			JWTPrincipal(jwtCredential.payload) else null
	}


	/**
	 * Calculate the expiration Date based on current time + the given validity
	 */
	@ExperimentalTime
	private fun getExpiration() =
		Clock.System.now()
			.plus(timeValid.toLong(), DateTimeUnit.HOUR)
			.toLocalDateTime(TimeZone.UTC)
			.toInstant(TimeZone.UTC)
			.toJavaInstant()
			.run {
				Date.from(this)
			}
}