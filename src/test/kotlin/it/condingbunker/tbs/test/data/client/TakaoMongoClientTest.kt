package it.condingbunker.tbs.test.data.client

import it.codingbunker.tbs.data.client.TakaoMongoClient

class TakaoMongoClientTest(
    serverAddress: String,
    databaseName: String
) : TakaoMongoClient(
    serverAddress,
    databaseName
) {

    fun dropDatabase() {
        mongoClientDB.drop()
    }
}