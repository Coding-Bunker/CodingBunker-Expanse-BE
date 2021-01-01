package it.codingbunker.tbs.utils

import io.ktor.config.*

fun ApplicationConfig.getPropertyString(path: String) =
    this.property(path).getString()