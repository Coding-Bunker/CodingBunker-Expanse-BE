package it.codingbunker.tbs.common

import io.ktor.application.*
import it.codingbunker.tbs.common.extension.getPropertyString

object Constants {

    private const val BASE_KEY = "ktor"

    object Url {
        const val BASE_API_URL = "/tbs/api"
    }

    object Session {
        const val LOGIN_SESSION_USER = "LOGIN_SESSION_USER"
    }

    object Database {
        private const val BASE_KEY = "database"

        private val BASE_DATABASE_KEY = arrayOf(
            Constants.BASE_KEY,
            BASE_KEY
        ).compileKey()

        val ADDRESS_DB_KEY = "$BASE_DATABASE_KEY.address"
        val USERNAME_DB_KEY = "$BASE_DATABASE_KEY.username"
        val PASSWORD_DB_KEY = "$BASE_DATABASE_KEY.password"
        val DRIVER_DB_KEY = "$BASE_DATABASE_KEY.driver"
    }

    object Security {
        private const val BASE_KEY = "security"

        private val BASE_SECRET_KEY = arrayOf(
            Constants.BASE_KEY,
            BASE_KEY
        ).compileKey()

        val AAD_CRYPT_SECRET_KEY = "$BASE_SECRET_KEY.AAD_CRYPT_SECRET_KEY"

        object Authentication {
            private const val BASE_KEY = "authentication"

            private val BASE_AUTHENTICATION_KEY = arrayOf(
                BASE_SECRET_KEY,
                BASE_KEY
            ).compileKey()

            object Discord {
                private const val BASE_KEY = "discord"

                private val BASE_DISCORD_KEY = arrayOf(
                    BASE_AUTHENTICATION_KEY,
                    BASE_KEY
                ).compileKey()

                val CLIENT_ID_KEY = "$BASE_DISCORD_KEY.clientId"
                val CLIENT_SECRET_KEY = "$BASE_DISCORD_KEY.clientSecret"
                val ADMIN_USER_ID = "$BASE_DISCORD_KEY.adminUserId"
            }
        }

        object Web {
            private const val BASE_KEY = "web"

            private val BASE_WEB_KEY = arrayOf(
                BASE_SECRET_KEY,
                BASE_KEY
            ).compileKey()

            val URL_REDIRECT = "$BASE_WEB_KEY.urlRedirect"
        }
    }

    object Authentication {
        private const val BASE_KEY = "authentication"

        private val BASE_AUTHENTICATION_KEY = arrayOf(
            Constants.BASE_KEY,
            BASE_KEY
        ).compileKey()

        val REALM = "$BASE_AUTHENTICATION_KEY.realm"

        object Discord {
            private const val BASE_KEY = "discord"

            private val BASE_DISCORD_KEY = arrayOf(
                BASE_AUTHENTICATION_KEY,
                BASE_KEY
            ).compileKey()

            val ACCESS_TOKEN_URL_KEY = "$BASE_DISCORD_KEY.accessTokenUrl"
            val AUTHORIZE_URL_KEY = "$BASE_DISCORD_KEY.authorizeUrl"
            val NAME = "$BASE_DISCORD_KEY.name"
        }
    }

    private fun Array<String>.compileKey(): String = joinToString(separator = ".")
}

fun ApplicationEnvironment.getProperty(key: String): String = config.getPropertyString(key)
fun ApplicationEnvironment.getPropertyList(key: String): List<String> = config.property(key).getList()
