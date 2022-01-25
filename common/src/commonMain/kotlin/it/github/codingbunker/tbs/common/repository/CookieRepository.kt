package it.github.codingbunker.tbs.common.repository

import com.russhwolf.settings.Settings
import com.russhwolf.settings.nullableString
import it.github.codingbunker.tbs.common.Constant.Session.LOGIN_SESSION_USER

class CookieRepository(private val settings: Settings) {

    var cookie: String? by settings.nullableString(LOGIN_SESSION_USER)
}
