package it.codingbunker.tbs.data.route.sec

import io.ktor.locations.*
import it.codingbunker.tbs.utils.Costant.Url.BASE_API_URL

@Location("$BASE_API_URL/auth/")
class AuthenticationRoute {
	@Location("/login")
	class LoginRoute(val parent: AuthenticationRoute)
}