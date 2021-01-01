package it.codingbunker.tbs.service

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import it.codingbunker.tbs.data.dto.DiscordGuildDTO
import it.codingbunker.tbs.data.repo.DiscordRepositoryInterface
import it.codingbunker.tbs.data.route.guild.DiscordGuildRoute
import it.codingbunker.tbs.utils.onFailure
import it.codingbunker.tbs.utils.onSuccess
import org.koin.ktor.ext.inject

@Suppress("unused")
@JvmOverloads
fun Application.discordGuildRoutes(testOrDebug: Boolean = false) {

    //https://github.com/ktorio/ktor-samples/blob/1.3.0/app/kweet/test/KweetApplicationTest.kt

    val discordRepository by inject<DiscordRepositoryInterface>()

    routing {
        if (testOrDebug) {
            trace {
                application.log.warn(it.buildText())
            }
        }

        put<DiscordGuildRoute> {
            val discordGuild = call.receive<DiscordGuildDTO>()

            if (discordGuild.guildId.isBlank()
                || discordGuild.guildName.isBlank()
            ) {
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

            discordRepository
                .updateDiscordGuild(discordGuildUpdate)
                .onSuccess {
                    call.respond(HttpStatusCode.OK, it)
                }
                .onFailure {
                    call.respond(HttpStatusCode.InternalServerError)
                }
        }

        delete<DiscordGuildRoute.DiscordGuildRouteId> {
            val guildExist = discordRepository.existDiscordGuildById(it.guildId)

            if (guildExist.not()) {
                call.respond(HttpStatusCode.NotFound)
                return@delete
            }

            discordRepository
                .deleteDiscordGuildById(it.guildId)
                .onSuccess {
                    call.respond(HttpStatusCode.OK)
                }.onFailure {
                    //TODO Nuova Eccezione per la delete
                    call.respond(HttpStatusCode.InternalServerError)
                }

        }

    }
}