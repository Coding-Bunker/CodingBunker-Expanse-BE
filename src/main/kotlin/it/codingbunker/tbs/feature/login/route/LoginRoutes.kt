package it.codingbunker.tbs.feature.login.route

import com.github.kittinunf.result.coroutines.getOrNull
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.html.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import it.codingbunker.tbs.common.Constants
import it.codingbunker.tbs.common.Constants.Session.LOGIN_SESSION_USER
import it.codingbunker.tbs.common.client.discord.OAuth2DiscordClient
import it.codingbunker.tbs.common.extension.onSuccess
import it.codingbunker.tbs.common.feature.withAnyRole
import it.codingbunker.tbs.common.html.page.bulmaHead
import it.codingbunker.tbs.common.model.session.UserSession
import it.codingbunker.tbs.feature.managment.repository.UserRepository
import it.codingbunker.tbs.feature.managment.table.RoleType
import it.codingbunker.tbs.feature.managment.table.UserDTO
import kotlinx.html.*
import org.koin.ktor.ext.inject
import java.time.Duration
import java.time.ZonedDateTime

@Location("${Constants.Url.BASE_API_URL}/login/{type?}")
class Login(val type: String = ""/*, val error: String? = null*/)

fun Application.loginRoutes() {
    routing {
        val discordClient by inject<OAuth2DiscordClient>()
        val userRepository by inject<UserRepository>()

        trace {
            application.log.info(it.buildText())
        }

        get("${Constants.Url.BASE_API_URL}/login") {
            call.loginPage(environment)
        }

        location<Login>() {

            optionalParam("error") {
                handle {
                    call.loginFailedPage(call.parameters.getAll("error").orEmpty())
                }
            }

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
                                call.loginFailedPage(listOf("User not found"))
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

                            call.loggedInSuccessResponse(principal)
                        }
                    } else {
                        call.loginPage(environment)
                    }
                }
            }
        }

        authenticate(LOGIN_SESSION_USER) {
            withAnyRole(RoleType.ADMIN) {
                get("${Constants.Url.BASE_API_URL}/home") {
                    val abc = call.sessions.get<UserSession>()

                    log.debug(abc?.accessToken)

                    call.respondText {
                        "Test OAuth"
                    }
                }
            }
        }
    }
}

private suspend fun ApplicationCall.loginPage(environment: ApplicationEnvironment) {
    respondHtml {
        bulmaHead {
            title { +"Login with" }
        }
        body {
            h1 {
                +"Login with:"
            }

            for (p in provideOAuth2Login(environment)) {
                p {
                    a(href = application.locations.href(Login(p.key))) {
                        +p.key
                    }
                }
            }
        }
    }
}

private suspend fun ApplicationCall.loginFailedPage(errors: List<String>) {
    respondHtml {
        head {
            title { +"Login with" }
        }
        body {
            h1 {
                +"Login error"
            }

            for (e in errors) {
                p {
                    +e
                }
            }
        }
    }
}

private suspend fun ApplicationCall.loggedInSuccessResponse(callback: OAuthAccessTokenResponse) {
    respondHtml {
        head {
            title { +"Logged in" }
        }
        body {
            h1 {
                +"You are logged in"
            }
            p {
                +"Your token is $callback"
            }
        }
    }
}

fun <T : Any> ApplicationCall.redirectUrl(t: T, secure: Boolean = true): String {
    val hostPort = request.host() + request.port().let { port -> if (port == 80) "" else ":$port" }
    val protocol = when {
        secure -> "https"
        else -> "http"
    }

    return "$protocol://$hostPort${application.locations.href(t)}"
}
