import Versions.exposedVersion
import Versions.junitVersion
import Versions.koinVersion
import Versions.kotlinVersion
import Versions.kotlinxDateTimeVersion
import Versions.ktorVersion
import Versions.logbackVersion
import Versions.mockkVersion

object Versions {
    const val androidMinSdk = 26
    const val androidCompileSdk = 31
    const val androidTargetSdk = androidCompileSdk

    const val kotlinVersion = "1.5.31"
    const val ktorVersion = "1.6.5"

    const val koinVersion = "3.1.3"
    const val logbackVersion = "1.2.7"
    const val exposedVersion = "0.36.1"

    const val kotlinxDateTimeVersion = "0.3.1"

//    const val rsocketVersion = "0.14.3"

    const val mockkVersion = "1.11.0"
    const val junitVersion = "5.8.2"

    const val kotlinterVersion = "3.3.0"

    const val gradleVersionsPlugin = "0.39.0"
}

object Deps {
    object Gradle {
        const val gradleVersionsPlugin = "com.github.ben-manes:gradle-versions-plugin:${Versions.gradleVersionsPlugin}"
    }

    object Kotlinx {
        const val kotlinJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
        const val dateTimeX = "org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDateTimeVersion"
    }

    object Ktor {
        const val core = "io.ktor:ktor-server-core:$ktorVersion"
        const val tomcat = "io.ktor:ktor-server-tomcat:$ktorVersion"
        const val htmlBuilder = "io.ktor:ktor-html-builder:$ktorVersion"
        const val serverHostCommon = "io.ktor:ktor-server-host-common:$ktorVersion"
        const val locations = "io.ktor:ktor-locations:$ktorVersion"
        const val websocket = "io.ktor:ktor-websockets:$ktorVersion"
        const val serialization = "io.ktor:ktor-serialization:$ktorVersion"
        const val auth = "io.ktor:ktor-auth:$ktorVersion"
        const val session = "io.ktor:ktor-server-sessions:$ktorVersion"

        const val metrics = "io.ktor:ktor-metrics:$ktorVersion"
    }

    object KtorClient {
        const val core = "io.ktor:ktor-client-core:$ktorVersion"
        const val serialization = "io.ktor:ktor-client-serialization:$ktorVersion"
        const val apache = "io.ktor:ktor-client-apache:$ktorVersion"
        const val logging = "io.ktor:ktor-client-logging:$ktorVersion"
    }

    object Koin {
        const val ktor = "io.insert-koin:koin-ktor:$koinVersion"
        const val logger = "io.insert-koin:koin-logger-slf4j:$koinVersion"
        const val ext = "io.insert-koin:koin-core-ext:3.0.2"
    }

    object KResult {
        const val result = "com.github.kittinunf.result:result:5.2.0"
        const val coroutine = "com.github.kittinunf.result:result-coroutines:4.0.0"
    }

    object Exposed {
        const val core = "org.jetbrains.exposed:exposed-core:$exposedVersion"
        const val dao = "org.jetbrains.exposed:exposed-dao:$exposedVersion"
        const val jdbc = "org.jetbrains.exposed:exposed-jdbc:$exposedVersion"
        const val javaTime = "org.jetbrains.exposed:exposed-java-time:$exposedVersion"
        const val apacheCommonText = "org.apache.commons:commons-text:1.8"

        const val postgresql = "org.postgresql:postgresql:42.3.1"
        const val h2 = "com.h2database:h2:1.4.199"
    }

//    object RSocket {
//        const val core = "io.rsocket.kotlin:rsocket-core:$rsocketVersion"
//        const val ktor = "io.rsocket.kotlin:rsocket-transport-ktor:$rsocketVersion"
//        const val ktorServer = "io.rsocket.kotlin:rsocket-ktor-server:$rsocketVersion"
//    }

    object Test {
        const val ktorServer = "io.ktor:ktor-server-tests:$ktorVersion"
        const val ktorServerHost = "io.ktor:ktor-server-test-host:$ktorVersion"
        const val ktorClient = "io.ktor:ktor-client-mock:$ktorVersion"
        const val ktorClientJvm = "io.ktor:ktor-client-mock-jvm:$ktorVersion"
        const val koin = "io.insert-koin:koin-test:$koinVersion"

        const val junit = "org.junit.jupiter:junit-jupiter-api:$junitVersion"
        const val junitEngine = "org.junit.jupiter:junit-jupiter-engine:$junitVersion"
        const val kotlinJunit = "org.jetbrains.kotlin:kotlin-test-junit5:$kotlinVersion"

        const val mockk = "io.mockk:mockk:$mockkVersion"
    }

    object Crypto {
        const val tink = "com.google.crypto.tink:tink:1.6.1"
    }

    object Log {
        const val logback = "ch.qos.logback:logback-classic:$logbackVersion"
    }
}