package it.codingbunker.tbs.service

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import it.codingbunker.tbs.data.bean.guild.DiscordGuildDTO
import it.codingbunker.tbs.data.bean.guild.DiscordGuildRoute
import it.codingbunker.tbs.data.repo.DiscordRepositoryInterface
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
            val discordGuildIdRequested = call.parameters[DiscordGuildRoute.DiscordGuildRouteId::guildId.name]

            if (discordGuildIdRequested == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val discordGuild = discordRepository.getDiscordGuildById(discordGuildIdRequested)

            if (discordGuild == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(discordGuild)
            }
        }

        patch<DiscordGuildRoute.DiscordGuildRouteId> {
            val discordGuildIdRequested = call.parameters[DiscordGuildRoute.DiscordGuildRouteId::guildId.name]

            if (discordGuildIdRequested.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest)
                return@patch
            }

            val guildExist = discordRepository.existDiscordGuildById(discordGuildIdRequested)

            if (guildExist.not()) {
                call.respond(HttpStatusCode.NotFound)
                return@patch
            }

            val discordGuildUpdate = call.receive<DiscordGuildDTO>()

            val discordGuild = discordRepository.updateDiscordGuild(discordGuildUpdate)

            call.respond(HttpStatusCode.OK, discordGuild)
        }

    }
}