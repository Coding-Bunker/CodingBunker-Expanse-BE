package it.github.codingbunker.tbs.common.repository

import io.ktor.client.engine.okhttp.*
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single { OkHttp.create() }
}