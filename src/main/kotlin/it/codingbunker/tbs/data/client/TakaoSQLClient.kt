package it.codingbunker.tbs.data.client

import it.codingbunker.tbs.data.bean.guild.DiscordGuilds
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class TakaoSQLClient(
    serverAddress: String,
    driverDB: String,
    usernameDB: String,
    passwordDB: String
) {

    val takaoDB =
        Database.connect(
            url = serverAddress,
            driver = driverDB,
            user = usernameDB,
            password = passwordDB
        )

    suspend fun checkAndActivateDB() {
        newSuspendedTransaction {
            SchemaUtils.createMissingTablesAndColumns(DiscordGuilds)
        }
    }

    companion object {
        private const val BASE_KEY = "ktor.database"

        const val ADDRESS_DB_KEY = "$BASE_KEY.address"
        const val USERNAME_DB_KEY = "$BASE_KEY.username"
        const val PASSWORD_DB_KEY = "$BASE_KEY.password "
        const val DRIVER_DB_KEY = "$BASE_KEY.driver"
    }
}