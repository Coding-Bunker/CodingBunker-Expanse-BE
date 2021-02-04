package it.codingbunker.tbs.service

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import it.codingbunker.tbs.data.dto.ProfileDTO
import it.codingbunker.tbs.data.dto.request.LoginRequest
import it.codingbunker.tbs.data.repo.AuthenticationInterface
import it.codingbunker.tbs.data.route.sec.AuthenticationRoute
import it.codingbunker.tbs.utils.JwtConfig
import it.codingbunker.tbs.utils.onFailure
import it.codingbunker.tbs.utils.onSuccess
import org.koin.ktor.ext.inject

fun Application.authenticationRoutes(testOrDebug: Boolean = false) {
	val jwtConfig by inject<JwtConfig>()

	val authenticationInterface by inject<AuthenticationInterface>()

	routing {
		if (testOrDebug) {
			trace {
				application.log.info(it.buildText())
			}
		}

		post<AuthenticationRoute.LoginRoute> {
			val loginRequest = call.receive<LoginRequest>()

			when (ProfileDTO.ProfileType.valueOf(loginRequest.profileType)) {
				ProfileDTO.ProfileType.BOT -> {
					authenticationInterface
						.findBotById(loginRequest.id)
						.onSuccess {
							jwtConfig.makeTokenBot(it)
								.onSuccess {
									call.respondText { it }
								}.onFailure {
									call.respond(HttpStatusCode.InternalServerError, "Error Creation JWT")
								}
						}.onFailure {
							call.respond(HttpStatusCode.Unauthorized, "Bot Id not found")
						}
				}
				else -> {
					call.respond(HttpStatusCode.Unauthorized)
				}
			}
		}
	}

}