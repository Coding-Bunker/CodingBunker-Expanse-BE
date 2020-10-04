package it.codingbunker.tbs.it.codingbunker.tbs.di

import io.ktor.application.*
import it.codingbunker.tbs.it.codingbunker.tbs.data.repo.TakaoMongoClient
import it.codingbunker.tbs.it.codingbunker.tbs.data.repo.TakaoMongoClient.Companion.ADDRESS_PROPERTY_KEY
import org.koin.core.KoinApplication
import org.koin.dsl.module

fun KoinApplication.loadKoinModules(environment: ApplicationEnvironment): KoinApplication{
    val module = module {
        single { TakaoMongoClient(environment.config.property(ADDRESS_PROPERTY_KEY).getString()) }
    }

   return modules(listOf(module))
}

