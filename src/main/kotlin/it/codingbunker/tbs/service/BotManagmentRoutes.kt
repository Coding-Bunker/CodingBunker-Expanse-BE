package it.codingbunker.tbs.service

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.*
import it.codingbunker.tbs.data.dto.request.bot.BotCreateRequest
import it.codingbunker.tbs.data.dto.response.ExceptionResponse
import it.codingbunker.tbs.data.dto.response.bot.BotIdKeyDTO
import it.codingbunker.tbs.data.repo.BotRepository
import it.codingbunker.tbs.data.route.bot.BotManagmentRoute
import it.codingbunker.tbs.data.route.sec.withAnyRole
import it.codingbunker.tbs.data.table.RoleType
import it.codingbunker.tbs.utils.onFailure
import it.codingbunker.tbs.utils.onSuccess
import org.koin.ktor.ext.inject
import java.util.*

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
				put<BotManagmentRoute.Edit> {
					val discordGuild = call.receive<BotCreateRequest>()

					if (discordGuild.roleList.isEmpty() || discordGuild.botName.isBlank()) {
						call.respond(HttpStatusCode.BadRequest)
						return@put
					}

					val result = botRepository.generateBot(
						discordGuild.botName,
						discordGuild.roleList
					)

					call.respond(
						HttpStatusCode.Created,
						BotIdKeyDTO(
							result.id,
							result.botKey
						)
					)
				}

				delete<BotManagmentRoute.BotManagmentRouteId> {
					if (it.botId.isBlank()) {
						call.respond(HttpStatusCode.BadRequest)
						return@delete
					}

					val exist = botRepository.existBotById(it.botId)

					if (exist) {
						botRepository.deleteBotById(it.botId)
							.onSuccess {
								call.respond(HttpStatusCode.OK)
							}.onFailure { ex ->
								call.respond(
									HttpStatusCode.InternalServerError,
									ExceptionResponse(ex::class.toString(), ex.stackTraceToString())
								)
							}
					} else {
						call.respond(HttpStatusCode.NotFound)
					}
				}
			}
		}
	}

}