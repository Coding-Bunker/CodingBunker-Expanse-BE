package it.codingbunker.tbs.common.client

import it.codingbunker.tbs.data.table.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

interface TakaoSQLInterface {

	suspend fun checkAndActivateDB()
}

class TakaoSQLClient(
	serverAddress: String,
	driverDB: String,
	usernameDB: String,
	passwordDB: String
) : TakaoSQLInterface {

	val takaoDB =
		Database.connect(
			url = serverAddress,
			driver = driverDB,
			user = usernameDB,
			password = passwordDB
		)

	override suspend fun checkAndActivateDB() {
		newSuspendedTransaction {
			SchemaUtils.createMissingTablesAndColumns(DiscordGuilds, Roles, Bots, BotsRoles)
			commit()

			Role.initTableValue()
		}
	}
}