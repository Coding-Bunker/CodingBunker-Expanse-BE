package it.codingbunker.tbs.data.client

import com.mongodb.client.MongoDatabase
import org.litote.kmongo.KMongo
import org.slf4j.LoggerFactory

open class TakaoMongoClient(
    serverAddress: String,
    databaseName: String
) {

    private val logger = LoggerFactory.getLogger(TakaoMongoClient::class.java)

    private val mongoClient = KMongo.createClient(serverAddress)
    val mongoClientDB: MongoDatabase = mongoClient.getDatabase(databaseName)

    companion object {
        const val ADDRESS_PROPERTY_KEY = "ktor.mongoDB.address"
        const val DATABASE_NAME_PROPERTY_KEY = "ktor.mongoDB.nameDB"
    }
}