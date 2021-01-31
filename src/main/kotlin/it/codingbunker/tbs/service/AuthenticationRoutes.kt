package it.codingbunker.tbs.service

import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.routing.*
import it.codingbunker.tbs.data.dto.request.LoginRequest
import it.codingbunker.tbs.data.route.sec.AuthenticationRoute
import it.codingbunker.tbs.utils.JwtConfig
import org.koin.ktor.ext.inject

fun Application.authenticationRoutes(testOrDebug: Boolean = false) {
	val jwtConfig by inject<JwtConfig>()

	routing {
		if (testOrDebug) {
			trace {
				application.log.info(it.buildText())
			}
		}

		post<AuthenticationRoute.LoginRoute> {
			val loginRequest = call.receive<LoginRequest>()
		}
	}

}