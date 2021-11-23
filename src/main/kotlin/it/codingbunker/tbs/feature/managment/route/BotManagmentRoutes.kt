package it.codingbunker.tbs.feature.managment.route

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.locations.post
import io.ktor.locations.put
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import it.codingbunker.tbs.common.Constants
import it.codingbunker.tbs.common.Constants.Session.LOGIN_SESSION_USER
import it.codingbunker.tbs.common.extension.onFailure
import it.codingbunker.tbs.common.extension.onSuccess
import it.codingbunker.tbs.common.feature.withAnyRole
import it.codingbunker.tbs.common.model.response.ExceptionResponse
import it.codingbunker.tbs.feature.managment.model.bot.BotIdKeyDTO
import it.codingbunker.tbs.feature.managment.model.bot.request.BotCreateRequest
import it.codingbunker.tbs.feature.managment.repository.BotRepository
import it.codingbunker.tbs.feature.managment.route.html.BotSettingsPageConstant.FORM_BOT_NAME_ID
import it.codingbunker.tbs.feature.managment.route.html.BotSettingsPageConstant.FORM_BOT_PERMISSION_ID
import it.codingbunker.tbs.feature.managment.route.html.getAllBotHtmlPage
import it.codingbunker.tbs.feature.managment.route.html.getConfigureNewBotHtmlPage
import it.codingbunker.tbs.feature.managment.route.html.showBotConfigurationResultHtmlPage
import it.codingbunker.tbs.feature.managment.table.RoleType
import kotlinx.coroutines.coroutineScope
import org.koin.ktor.ext.inject

@Location("${Constants.Url.BASE_API_URL}/managment/bot")
class BotManagmentRoute {

    @Location("/edit")
    class Edit(val parent: BotManagmentRoute)

    @Location("/{botId}")
    class BotManagmentRouteId(val parent: BotManagmentRoute, val botId: String) {

        @Location("/delete")
        class Delete(val parent: BotManagmentRoute)
    }

    @Location("/settings")
    class Settings(val parent: BotManagmentRoute) {

        @Location("/create")
        class Create(val parent: Settings)
    }
}

fun Application.botManagmentRoutes(testOrDebug: Boolean = false) {

    val botRepository by inject<BotRepository>()
    val botManagmentController by inject<BotManagmentController>()

    routing {
        if (testOrDebug) {
            trace {
                application.log.info(it.buildText())
            }
        }

        authenticate(LOGIN_SESSION_USER) {
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
//                    botManagmentController.deleteBot()
                }

//                get<BotManagmentRoute.BotManagmentRouteId.Delete> {
//
//                }

                get<BotManagmentRoute.Settings> {
                    botRepository.getAllBots()
                        .onSuccess {
                            call.getAllBotHtmlPage(it)
                        }.onFailure {
                            call.respond(HttpStatusCode.InternalServerError)
                        }
                }

                get<BotManagmentRoute.Settings.Create> {
                    call.getConfigureNewBotHtmlPage()
                }

                post<BotManagmentRoute.Settings.Create> {
                    val formParameters = call.receiveParameters()
                    val botName: String? = formParameters[FORM_BOT_NAME_ID]
                    val permissionRequestedList: Set<RoleType>? = formParameters[FORM_BOT_PERMISSION_ID]?.toInt()?.let {
                        setOf(RoleType.values()[it])
                    }

                    if (botName.isNullOrBlank()) {
                        call.getConfigureNewBotHtmlPage()
                        return@post
                    }

                    if (permissionRequestedList.isNullOrEmpty()) {
                        call.getConfigureNewBotHtmlPage()
                        return@post
                    }

                    val result = botRepository.generateBot(
                        botName, permissionRequestedList
                    )

                    call.showBotConfigurationResultHtmlPage(result)
                }
            }
        }
    }

    suspend fun PipelineContext<Unit, ApplicationCall>.deleteBot(botId: String) = coroutineScope {
        if (botId.isBlank()) {
            call.respond(HttpStatusCode.BadRequest)
            return@coroutineScope
        }

        val exist = botRepository.existBotById(botId)

        if (exist) {
            botRepository.deleteBotById(botId).onSuccess {
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
