package it.codingbunker.tbs.it.codingbunker.tbs.data.repo

import com.mongodb.client.MongoDatabase
import it.codingbunker.tbs.it.codingbunker.tbs.data.DiscordEntity
import org.koin.core.KoinComponent
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import org.slf4j.LoggerFactory

class TakaoMongoClient(
    serverAddress: String
) : KoinComponent{

    val logger= LoggerFactory.getLogger(TakaoMongoClient::class.java)

    private val mongoClient: MongoDatabase = KMongo.createClient(serverAddress).getDatabase("takao_expanse_db")

    companion object{
        const val ADDRESS_PROPERTY_KEY = "ktor.mongoDB.address"
    }
}