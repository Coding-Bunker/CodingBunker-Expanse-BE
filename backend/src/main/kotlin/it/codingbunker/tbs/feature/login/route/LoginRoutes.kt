package it.codingbunker.tbs.feature.login.route

import com.github.kittinunf.result.getOrNull
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*
import it.codingbunker.tbs.common.client.discord.OAuth2DiscordClient
import it.codingbunker.tbs.common.feature.withAnyRole
import it.codingbunker.tbs.common.model.session.UserSession
import it.codingbunker.tbs.feature.managment.repository.UserRepository
import it.codingbunker.tbs.feature.managment.table.UserDTO
import it.github.codingbunker.tbs.common.Constant
import it.github.codingbunker.tbs.common.Constant.AppLink.HOST_NAME
import it.github.codingbunker.tbs.common.Constant.Session.LOGIN_SESSION_USER
import it.github.codingbunker.tbs.common.model.*
import it.github.codingbunker.tbs.common.util.onFailure
import it.github.codingbunker.tbs.common.util.onSuccess
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.title
import org.koin.ktor.ext.inject
import java.time.Duration
import java.time.ZonedDateTime

@Location("${Constant.Url.BASE_API_URL}/login/{type?}")
class Login(val type: String = ""/*, val error: String? = null*/)

@Location("${Constant.Url.BASE_API_URL}/login/success")
class LoginSuccess

fun Application.loginRoutes() {
    routing {
        val discordClient by inject<OAuth2DiscordClient>()
        val userRepository by inject<UserRepository>()

        trace {
            application.log.info(it.buildText())
        }

        get("${Constant.Url.BASE_API_URL}/login") {
            val loginList = provideOAuth2Login(environment).map {
                val routeName = LoginRoute.getRoute(it.key)
                return@map if (routeName != null) {
                    LoginRouteDto(
                        routeName,
                        application.locations.href(Login(it.key))
                    )
                } else {
                    null
                }
            }

            call.respond(loginList)
        }

        location<Login> {

//            optionalParam("error") {
//                handle {
//                    call.loginFailedPage(call.parameters.toMap()["error"].orEmpty())
//                }
//            }

            authenticate("discord") {
                handle {
                    val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()

                    if (principal != null) {

                        discordClient.getUser(principal).onSuccess { discordUser ->
                            val userExist = userRepository.existUserByDiscordId(discordUser.id)

                            val userDTO: UserDTO? = if (userExist) {
                                userRepository.findUserByDiscordId(discordUser.id).getOrNull()
                            } else {
                                userRepository.generateUserByDiscordUser(discordUser)
                            }

                            if (userDTO == null) {
                                call.respond(
                                    HttpStatusCode.Unauthorized,
                                    BackendException(ExceptionCode.LOGIN_ERROR, "User not found")
                                )
                                return@onSuccess
                            }

                            val userSession = UserSession(
                                principal.accessToken,
                                principal.tokenType,
                                ZonedDateTime
                                    .now()
                                    .plus(Duration.ofMillis(principal.expiresIn))
                                    .toInstant()
                                    .toEpochMilli(),
                                userDTO
                            )

                            call.sessions.set(userSession)

                            val urlRedirect = application.locations.href(LoginSuccess())

                            call.respondRedirect(urlRedirect)
                        }.onFailure {
                            application.log.error(it)

                            call.respond(
                                HttpStatusCode.Unauthorized,
                                BackendException(
                                    ExceptionCode.LOGIN_DISCORD_ERROR,
                                    "Error Login Discord " + it.stackTrace.map { stack -> stack.toString() }.toString()
                                )
                            )
                        }
                    } else {
                        call.respond(
                            HttpStatusCode.Unauthorized,
                            BackendException(ExceptionCode.LOGIN_ERROR, "Error Principal")
                        )
                    }
                }
            }
        }

        authenticate(LOGIN_SESSION_USER) {
            withAnyRole(RoleType.ADMIN, RoleType.USER) {
                get<LoginSuccess> {
                    val id = call.sessionId<UserSession>()

                    val urlRedirect = url {
                        protocol = URLProtocol(Constant.AppLink.PROTOCOL_NAME, Constant.AppLink.PROTOCOL_PORT)
                        host = HOST_NAME
                        path(Constant.Url.LOGIN_SUCCESS_PATH)
                        if (id != null) {
                            parameters[LOGIN_SESSION_USER] = id
                        }
                    }

                    call.respondRedirect(urlRedirect)
                }
            }
        }
    }
}

private suspend fun ApplicationCall.loggedInSuccessResponse() {
    respondHtml {
        head {
            title { +"Logged in" }
        }
        body {
            h1 {
                +"You are logged in"
            }
        }
    }
}
