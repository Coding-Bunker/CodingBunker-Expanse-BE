package utils

import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.server.engine.*
import io.ktor.server.testing.*
import org.slf4j.LoggerFactory

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
    confName: String = "application-test.conf",
    configure: ApplicationEngineEnvironmentBuilder.() -> Unit = {}
): ApplicationEngineEnvironment =
    applicationEngineEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load(confName))
        log = LoggerFactory.getLogger("ktor.test")
        configure()
    }