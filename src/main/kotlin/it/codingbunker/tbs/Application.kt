package it.codingbunker.tbs

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.jackson.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.server.engine.*
import io.ktor.websocket.*
import it.codingbunker.tbs.data.client.TakaoSQLClient
import it.codingbunker.tbs.data.dto.ProfileDTO
import it.codingbunker.tbs.data.route.sec.RoleBasedAuthorization
import it.codingbunker.tbs.di.loadKoinModules
import it.codingbunker.tbs.utils.JwtConfig
import kotlinx.coroutines.runBlocking
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import org.koin.logger.slf4jLogger
import org.slf4j.event.Level
import java.time.Duration

fun main(args: Array<String>) {
    io.ktor.server.tomcat.EngineMain.main(args)
}

@JvmOverloads
fun Application.mainModule(testing: Boolean = false) {
    install(Locations) {
    }

    install(Compression) {
        gzip {
            priority = 1.0
        }
        deflate {
            priority = 10.0
            minimumSize(1024) // condition
        }
    }

    install(CallLogging) {
        level = Level.DEBUG
        filter { call -> call.request.path().startsWith("/") }
    }

    install(StatusPages) {
        exception<MissingKotlinParameterException> { cause ->
            call.respond(HttpStatusCode.BadRequest)
        }

    }

    install(Koin) {
        slf4jLogger()
        loadKoinModules(environment)
    }

    val client by inject<TakaoSQLClient>()
    runBlocking {
        client.checkAndActivateDB()
    }

    install(DataConversion)

    // https://ktor.io/servers/features/https-redirect.html#testing
    if (!testing) {
        /*install(HttpsRedirect) {
            // The port to redirect to. By default 443, the default HTTPS port.
            sslPort = 443
            // 301 Moved Permanently, or 302 Found redirect.
            permanentRedirect = false
        }*/
    }

    install(ShutDownUrl.ApplicationCallFeature) {
        // The URL that will be intercepted (you can also use the application.conf's ktor.deployment.shutdown.url key)
        shutDownUrl = "/ktor/application/shutdown"
        // A function that will be executed to get the exit code of the process
        exitCodeSupplier = { 0 } // ApplicationCall.() -> Int
    }

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    install(Authentication) {
        val jwtConfig by inject<JwtConfig>()

        jwt {
            verifier(jwtConfig.jwtVerifier)
            validate { jwtCredential ->
                //TODO INSERIRE CONVERSIONE JWT + DECRYPT
                jwtConfig.convertJWTCredential(jwtCredential)

//                if (jwtCredential.payload.claims.contains(BotJWTDTO::profileType.name))
//                    JWTPrincipal(jwtCredential.payload) else null
            }
        }
    }

    install(RoleBasedAuthorization) {
        getRoles {
            (it as ProfileDTO).role
        }
    }

    install(ContentNegotiation) {
        jackson {
            registerKotlinModule()
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }


    /*
    runBlocking {
        // Sample for making a HTTP Client request

        val message = client.post<JsonSampleClass> {
            url("http://127.0.0.1:8080/path/to/endpoint")
            contentType(ContentType.Application.Json)
            body = JsonSampleClass(hello = "world")
        }
        }
        */

    /*routing {

        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        *//*
        get("/html-dsl") {
            call.respondHtml {
                body {

                    h1 { +"HTML" }
                    ul {
                        for (n in 1..10) {
                            li { +"$n" }
                        }
                    }

                    button {
                        style {
                            backgroundColor = Color.blue
                            color = Color.white
                        }

                        text("Premi qua")
                    }
                }
            }
        }

        get("/styles.css") {
            call.respondCss {
                body {
                    backgroundColor = Color.red
                }
                p {
                    fontSize = 2.em
                }
                rule("p.myclass") {
                    color = Color.blue
                }
            }
        }*//*

        // Static feature. Try to access `/static/ktor_logo.svg`
        static("/static") {
            resources("static")
        }

        get<MyLocation> {
            call.respondText("Location: name=${it.name}, arg1=${it.arg1}, arg2=${it.arg2}")
        }
        // Register nested routes
        get<Type.Edit> {
            call.respondText("Inside $it")
        }
        get<Type.List> {
            call.respondText("Inside $it")
        }

        install(StatusPages) {
            exception<AuthenticationException> { cause ->
                call.respond(HttpStatusCode.Unauthorized)
            }
            exception<AuthorizationException> { cause ->
                call.respond(HttpStatusCode.Forbidden)
            }

        }

        webSocket("/myws/echo") {
            send(Frame.Text("Hi from server"))
            while (true) {
                val frame = incoming.receive()
                if (frame is Frame.Text) {
                    send(Frame.Text("Client said: " + frame.readText()))
                }
            }
        }

        get("/json/jackson") {
            call.respond(mapOf("hello" to "world"))
        }

        get("/test/response") {
            call.respond(Type.List(Type("abc"), 2))
        }
    }*/
}

/*@Location("/location/{name}")
class MyLocation(val name: String, val arg1: Int = 42, val arg2: String = "default")

@Location("/type/{name}")
data class Type(val name: String) {
    @Location("/edit")
    data class Edit(val type: Type)

    @Location("/list/{page}")
    data class List(val type: Type, val page: Int)
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()

data class JsonSampleClass(val hello: String)

fun FlowOrMetaDataContent.styleCss(builder: CSSBuilder.() -> Unit) {
    style(type = ContentType.Text.CSS.toString()) {
        +CSSBuilder().apply(builder).toString()
    }
}

fun CommonAttributeGroupFacade.style(builder: CSSBuilder.() -> Unit) {
    this.style = CSSBuilder().apply(builder).toString().trim()
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}*/
