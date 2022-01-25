package it.github.codingbunker.tbs.common.remote

import io.ktor.client.*
import io.ktor.client.request.*
import it.github.codingbunker.tbs.common.Constant
import it.github.codingbunker.tbs.common.model.BotDTO
import it.github.codingbunker.tbs.common.model.LoginRouteDto

class ExpanseApi(
    private val client: HttpClient,
) {
    //TODO CHANGE THIS TO BUILD CONFIG
    suspend fun getAvailableLogin(): List<LoginRouteDto> =
        client.get("${Constant.Url.BASE_API_URL}/login")

    suspend fun getAllBots(): List<BotDTO> =
        client.get("${Constant.Url.BASE_API_URL}/managment/bot")
}