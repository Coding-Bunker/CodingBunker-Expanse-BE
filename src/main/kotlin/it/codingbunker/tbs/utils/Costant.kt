package it.codingbunker.tbs.utils

object Costant {

	object Url {
		const val BASE_API_URL = "/tbs/api"
	}

	object Database {
		private const val BASE_DB_KEY = "ktor.database"

		const val ADDRESS_DB_KEY = "$BASE_DB_KEY.address"
		const val USERNAME_DB_KEY = "$BASE_DB_KEY.username"
		const val PASSWORD_DB_KEY = "$BASE_DB_KEY.password "
		const val DRIVER_DB_KEY = "$BASE_DB_KEY.driver"
	}

	object Secret {
		private const val BASE_KEY = "ktor.secret"

		const val AAD_CRYPT_SECRET_KEY = "$BASE_KEY.AAD_CRYPT_SECRET_KEY"
	}

	object Authentication {
		private const val BASE_KEY = "ktor.authentication"

		const val REALM = "${BASE_KEY}.realm"
	}


}