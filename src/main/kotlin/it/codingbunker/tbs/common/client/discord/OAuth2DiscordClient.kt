package it.codingbunker.tbs.common.client.discord

import com.github.kittinunf.result.coroutines.SuspendableResult
import io.ktor.auth.*
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import it.codingbunker.tbs.common.client.discord.model.DiscordOAuth2User

class OAuth2DiscordClient {

    companion object {
        private const val DISCORD_REST_VERSION = 9
        private const val BASE_API_URL = "https://discord.com/api/v$DISCORD_REST_VERSION"
    }

    private val client = HttpClient() {
        install(Logging)
        defaultRequest {
            host + BASE_API_URL
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer(
                kotlinx.serialization.json.Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    suspend fun getUser(token: OAuthAccessTokenResponse.OAuth2): SuspendableResult<DiscordOAuth2User, Exception> {
        return SuspendableResult.of {
            client.get("$BASE_API_URL/users/@me") {
                headers {
                    append(HttpHeaders.Authorization, "${token.tokenType} ${token.accessToken}")
                }
            }
        }
    }
}