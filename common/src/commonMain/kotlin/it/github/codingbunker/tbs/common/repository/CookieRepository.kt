package it.github.codingbunker.tbs.common.repository

expect class CookieRepository() {
    fun getCookie(): String?
    fun saveCookie(value: String)
    fun deleteCookie()
}