package it.github.codingbunker.tbs.common

import it.github.codingbunker.tbs.common.BuildConfigGenerated.SERVER_PORT
import it.github.codingbunker.tbs.common.BuildConfigGenerated.SERVER_URL

object Constant {

    object Url {
        const val BASE_API_URL = "/tbs/api"
        const val LOGIN_SUCCESS_PATH = "login/success"

        const val SERVER_URL_ENDPOINT = "$SERVER_URL:$SERVER_PORT"
    }

    object Session {
        const val COOKIE_STORE = "COOKIE_STORE"
        const val LOGIN_SESSION_USER = "LOGIN_SESSION_USER"
    }

    object AppLink {
        const val HOST_NAME = "expansebe"
        const val PROTOCOL_NAME = "codingbunker"
        const val PROTOCOL_PORT = 80
    }
}