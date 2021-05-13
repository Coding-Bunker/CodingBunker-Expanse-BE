package it.codingbunker.tbs.feature.managment.route

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import it.codingbunker.tbs.common.Costant
import it.codingbunker.tbs.common.extension.onFailure
import it.codingbunker.tbs.common.extension.onSuccess
import it.codingbunker.tbs.common.feature.withAnyRole
import it.codingbunker.tbs.common.model.response.ExceptionResponse
import it.codingbunker.tbs.feature.managment.model.bot.BotIdKeyDTO
import it.codingbunker.tbs.feature.managment.model.bot.request.BotCreateRequest
import it.codingbunker.tbs.feature.managment.repository.BotRepository
import it.codingbunker.tbs.feature.managment.table.RoleType
import org.koin.ktor.ext.inject

@Location("${Costant.Url.BASE_API_URL}/managment/bot")
class BotManagmentRoute {

    @Location("/edit")
    class Edit(val parent: BotManagmentRoute)

    @Location("/{botId}")
    class BotManagmentRouteId(val parent: BotManagmentRoute, val botId: String)
}

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
                        discordGuild.botName, discordGuild.roleList
                    )

                    call.respond(
                        HttpStatusCode.Created,
                        BotIdKeyDTO(
                            result.id, result.botKey
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
                        botRepository.deleteBotById(it.botId).onSuccess {
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
