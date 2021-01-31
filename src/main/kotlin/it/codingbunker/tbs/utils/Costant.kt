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

	object Jwt {
		private const val BASE_JWT_KEY = "ktor.jwt"

		const val TIME_VALIDATION_JWT_KEY = "$BASE_JWT_KEY.timeValidation"
		const val ISSUER_BOT_JWT_KEY = "$BASE_JWT_KEY.issuer"
		const val SUBJECT_BOT_JWT_KEY = "$BASE_JWT_KEY.subject"
		const val SECRET_BOT_JWT_KEY = "$BASE_JWT_KEY.secret"
	}

	object Secret {
		private const val BASE_KEY = "ktor.secret"

		const val AAD_CRYPT_SECRET_KEY = "$BASE_KEY.AAD_CRYPT_SECRET_KEY"
	}


}