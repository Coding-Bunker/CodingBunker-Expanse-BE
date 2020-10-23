package it.codingbunker.tbs.service

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import it.codingbunker.tbs.data.bean.guild.DiscordGuild
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

        put<DiscordGuild> {
            val discordGuild = call.receive<DiscordGuild>()

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

        get<DiscordGuild.DiscordGuildGet> {
            val discordGuildIdRequested = call.parameters.getAll(DiscordGuild.DiscordGuildGet::serverId.name)

            if (discordGuildIdRequested == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val discordGuild = discordRepository.getDiscordGuildById(discordGuildIdRequested.first())

            if (discordGuild == null) {
                call.respond(HttpStatusCode.NotFound)
            } else {
                call.respond(discordGuild)
            }
        }
    }
}