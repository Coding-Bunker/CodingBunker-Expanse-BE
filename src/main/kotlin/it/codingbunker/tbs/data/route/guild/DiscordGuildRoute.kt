package it.codingbunker.tbs.data.route.guild

import io.ktor.locations.*
import it.codingbunker.tbs.utils.Costant.Url.BASE_API_URL

@Location("$BASE_API_URL/discord/guild/")
class DiscordGuildRoute {
    @Location("/{guildId}")
    class DiscordGuildRouteId(val parent: DiscordGuildRoute, var guildId: String)
}