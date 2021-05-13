package it.codingbunker.tbs.feature.login.route

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import it.codingbunker.tbs.common.Constants
import it.codingbunker.tbs.common.getProperty

fun provideOAuth2Login(environment: ApplicationEnvironment): Map<String, OAuthServerSettings.OAuth2ServerSettings> {
    return listOf(
        OAuthServerSettings.OAuth2ServerSettings(
            name = environment.getProperty(Constants.Authentication.Discord.NAME),
            authorizeUrl = environment.getProperty(Constants.Authentication.Discord.AUTHORIZE_URL_KEY),
            accessTokenUrl = environment.getProperty(Constants.Authentication.Discord.ACCESS_TOKEN_URL_KEY),
            clientId = environment.getProperty(Constants.Security.Authentication.Discord.CLIENT_ID_KEY),
            clientSecret = environment.getProperty(Constants.Security.Authentication.Discord.CLIENT_SECRET_KEY),
            requestMethod = HttpMethod.Post
        )
    ).associateBy { it.name }
}