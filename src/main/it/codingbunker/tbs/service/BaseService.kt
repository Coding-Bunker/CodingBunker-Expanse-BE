package it.codingbunker.tbs.it.codingbunker.tbs.service

import io.ktor.application.*
import it.codingbunker.tbs.it.codingbunker.tbs.data.repo.TakaoMongoClient
import org.koin.ktor.ext.inject

fun Application.base(){
    val clientMongo: TakaoMongoClient by inject()
}