package it.codingbunker.tbs.data.route.bot

import io.ktor.locations.*
import it.codingbunker.tbs.utils.Costant.Url.BASE_API_URL

@Location("$BASE_API_URL/managment/bot")
class BotManagmentRoute {

	@Location("/edit")
	class Edit

	@Location("/{botId}")
	class BotManagmentRouteId(val parent: BotManagmentRoute, val botId: String)
}