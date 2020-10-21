package it.condingbunker.tbs.test.utilstest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.server.engine.*
import io.ktor.server.testing.*
import org.slf4j.LoggerFactory

const val CONFIG_NAME: String = "application-test.conf"

fun <R> withRealTestApplication(
    moduleFunction: Application.() -> Unit,
    configure: TestApplicationEngine.Configuration.() -> Unit = {},
    test: TestApplicationEngine.() -> R
): R {
    return withApplication(createRealTestEnvironment(), configure) {
        moduleFunction(application)
        test()
    }
}

fun createRealTestEnvironment(
    confName: String = CONFIG_NAME,
    configure: ApplicationEngineEnvironmentBuilder.() -> Unit = {}
): ApplicationEngineEnvironment =
    applicationEngineEnvironment {
        config = loadConfig(confName)
        log = LoggerFactory.getLogger("ktor.test")
        configure()
    }

fun loadConfig(confName: String = CONFIG_NAME) = HoconApplicationConfig(ConfigFactory.load(confName))

fun Map<String, Any>.toJson() = jacksonObjectMapper().writeValueAsString(this)