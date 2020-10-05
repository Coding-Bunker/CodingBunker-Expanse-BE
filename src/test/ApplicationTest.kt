package it.codingbunker.tbs

import io.ktor.http.*
import io.ktor.server.testing.*
import utils.withRealTestApplication
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


}
