package it.codingbunker.tbs

import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.engine.*
import io.ktor.server.testing.*
import org.junit.BeforeClass
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.slf4j.LoggerFactory
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() {
        withRealTestApplication(
            {
                moduleUser(testing = true)
            }
        ) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("HELLO WORLD!", response.content)
            }
        }
    }

    public fun <R> withRealTestApplication(
        moduleFunction: Application.() -> Unit,
        configure: TestApplicationEngine.Configuration.() -> Unit = {},
        test: TestApplicationEngine.() -> R
    ): R {
        return withApplication(createRealTestEnvironment(), configure) {
            moduleFunction(application)
            test()
        }
    }

    public fun createRealTestEnvironment(
        configure: ApplicationEngineEnvironmentBuilder.() -> Unit = {}
    ): ApplicationEngineEnvironment =
        applicationEngineEnvironment {
            config = HoconApplicationConfig(ConfigFactory.load("application-test.conf"))
            log = LoggerFactory.getLogger("ktor.test")
            configure()
        }
}
