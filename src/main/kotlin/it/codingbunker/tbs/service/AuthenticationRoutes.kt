package it.codingbunker.tbs.service

import io.ktor.application.*
import io.ktor.routing.*
import it.codingbunker.tbs.data.repo.BotRepository
import org.koin.ktor.ext.inject

fun Application.authenticationRoutes(testOrDebug: Boolean = false) {

	val authenticationInterface by inject<BotRepository>()

	routing {
		if (testOrDebug) {
			trace {
				application.log.info(it.buildText())
			}
		}
	}

}