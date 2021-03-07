package it.codingbunker.tbs.service

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import it.codingbunker.tbs.data.dto.request.bot.BotCreateRequest
import it.codingbunker.tbs.data.dto.response.BotCreateResponse
import it.codingbunker.tbs.data.repo.BotRepository
import it.codingbunker.tbs.data.route.bot.BotManagmentRoute
import it.codingbunker.tbs.data.route.sec.withAnyRole
import it.codingbunker.tbs.data.table.RoleType
import org.koin.ktor.ext.inject

fun Application.botManagmentRoutes(testOrDebug: Boolean = false) {

	val botRepository by inject<BotRepository>()

	routing {
		if (testOrDebug) {
			trace {
				application.log.info(it.buildText())
			}
		}

		authenticate {
			withAnyRole(RoleType.ADMIN) {
				put<BotManagmentRoute> {
					val discordGuild = call.receive<BotCreateRequest>()

					val result = botRepository.generateBot(
						discordGuild.botName,
						discordGuild.roleList
					)

					call.respond(
						BotCreateResponse(
							result.id,
							result.botKey
						)
					)
				}
			}
		}
	}

}