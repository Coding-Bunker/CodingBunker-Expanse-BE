package it.github.codingbunker.tbs.common.remote

import io.ktor.client.*
import io.ktor.client.request.*
import it.github.codingbunker.tbs.common.Constant
import it.github.codingbunker.tbs.common.model.LoginRouteDto

class ExpanseApi(
    private val client: HttpClient,
    val baseUrl: String
) {
    suspend fun getAvailableLogin(): List<LoginRouteDto> =
        client.get("${Constant.Url.BASE_API_URL}/login")
}