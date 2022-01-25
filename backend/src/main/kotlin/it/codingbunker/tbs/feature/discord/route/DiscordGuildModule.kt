package it.codingbunker.tbs.feature.discord.route

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.locations.patch
import io.ktor.locations.put
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import it.codingbunker.tbs.common.feature.withAnyRole
import it.codingbunker.tbs.data.repo.DiscordRepositoryInterface
import it.codingbunker.tbs.feature.discord.model.DiscordGuildDTO
import it.github.codingbunker.tbs.common.Constant
import it.github.codingbunker.tbs.common.model.RoleType
import it.github.codingbunker.tbs.common.util.onFailure
import it.github.codingbunker.tbs.common.util.onSuccess
import org.koin.ktor.ext.inject

@Location("${Constant.Url.BASE_API_URL}/discord/guild")
class DiscordGuildRoute {
    @Location("/{guildId}")
    class DiscordGuildRouteId(val parent: DiscordGuildRoute, var guildId: String)
}

@Suppress("unused")
@JvmOverloads
fun Application.discordGuildRoutes(testOrDebug: Boolean = false) {

    val discordRepository by inject<DiscordRepositoryInterface>()

    routing {
        if (testOrDebug) {
            trace {
                application.log.info(it.buildText())
            }
        }

        authenticate {
            withAnyRole(RoleType.BOT_DISCORD) {
                put<DiscordGuildRoute> {
                    val discordGuild = call.receive<DiscordGuildDTO>()

                    if (discordGuild.guildId.isBlank() || discordGuild.guildName.isBlank()) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@put
                    }

                    if (discordRepository.existDiscordGuildById(discordGuild.guildId)) {
                        call.respond(HttpStatusCode.Conflict)
                    } else {
                        discordRepository.createDiscordGuild(discordGuild)
                        call.respond(HttpStatusCode.Created)
                    }
                }

                get<DiscordGuildRoute.DiscordGuildRouteId> {
                    val discordGuild = discordRepository.getDiscordGuildById(it.guildId)

                    if (discordGuild == null) {
                        call.respond(HttpStatusCode.NotFound)
                    } else {
                        call.respond(discordGuild)
                    }
                }

                patch<DiscordGuildRoute.DiscordGuildRouteId> {
                    val discordGuildIdRequested = it.guildId

                    if (discordGuildIdRequested.isBlank()) {
                        call.respond(HttpStatusCode.BadRequest)
                        return@patch
                    }

                    val guildExist = discordRepository.existDiscordGuildById(discordGuildIdRequested)

                    if (guildExist.not()) {
                        call.respond(HttpStatusCode.NotFound)
                        return@patch
                    }

                    val discordGuildUpdate = call.receive<DiscordGuildDTO>()

                    discordRepository.updateDiscordGuild(discordGuildUpdate).onSuccess {
                        call.respond(HttpStatusCode.OK, it)
                    }.onFailure {
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }

                delete<DiscordGuildRoute.DiscordGuildRouteId> {
                    val guildExist = discordRepository.existDiscordGuildById(it.guildId)

                    if (guildExist.not()) {
                        call.respond(HttpStatusCode.NotFound)
                        return@delete
                    }

                    discordRepository.deleteDiscordGuildById(it.guildId).onSuccess {
                        call.respond(HttpStatusCode.OK)
                    }.onFailure {
                        // TODO Nuova Eccezione per la delete
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }
            }
        }
    }
}
