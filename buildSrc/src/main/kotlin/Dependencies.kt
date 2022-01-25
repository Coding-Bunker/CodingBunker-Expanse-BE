import Versions.androidXCryptoVersion
import Versions.datastoreXVersion
import Versions.dateTimeXVersion
import Versions.exposedVersion
import Versions.junitVersion
import Versions.kermitVersion
import Versions.koinVersion
import Versions.kotlinCoroutines
import Versions.kotlinVersion
import Versions.kotlinxDateTimeVersion
import Versions.ktorVersion
import Versions.logbackVersion
import Versions.mockkVersion
import Versions.preferencesVersion

object Versions {
    const val androidMinSdk = 26
    const val androidCompileSdk = 31
    const val androidTargetSdk = androidCompileSdk

    const val kotlinVersion = "1.6.0"
    const val kotlinCoroutines = "1.5.2"
    const val ktorVersion = "1.6.5"
    const val kotlinxSerialization = "1.3.1"

    const val koinVersion = "3.1.3"
    const val logbackVersion = "1.2.7"
    const val exposedVersion = "0.36.1"

    const val preferencesVersion = "0.8.1"
    const val datastoreXVersion = "1.0.0"

    const val kotlinxDateTimeVersion = "0.3.1"

//   const val rsocketVersion = "0.14.3"

    const val mockkVersion = "1.11.0"
    const val junitVersion = "5.8.2"

    const val kotlinterVersion = "3.3.0"

    const val gradleVersionsPlugin = "0.39.0"

    const val kermitVersion = "1.0.0"

    const val androidXCryptoVersion = "1.0.0"
    const val dateTimeXVersion = "0.3.2"
}

object Deps {
    object Gradle {
        const val gradleVersionsPlugin = "com.github.ben-manes:gradle-versions-plugin:${Versions.gradleVersionsPlugin}"
    }

    object Kotlinx {
        const val kotlinJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion"
        const val dateTimeX = "org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDateTimeVersion"
        const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutines"
        const val serializationCore =
            "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.kotlinxSerialization}"
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
        const val json = "io.ktor:ktor-client-json:$ktorVersion"
        const val okhttp = "io.ktor:ktor-client-okhttp:$ktorVersion"
    }

    object Koin {
        const val core = "io.insert-koin:koin-core:$koinVersion"
        const val test = "io.insert-koin:koin-test:$koinVersion"
        const val ktor = "io.insert-koin:koin-ktor:$koinVersion"
        const val logger = "io.insert-koin:koin-logger-slf4j:$koinVersion"
        const val ext = "io.insert-koin:koin-core-ext:3.0.2"
        const val android = "io.insert-koin:koin-android:$koinVersion"
        const val compose = "io.insert-koin:koin-androidx-compose:$koinVersion"
    }

    object KResult {
        const val result = "com.github.kittinunf.result:result:5.2.0"
    }

    object Exposed {
        const val core = "org.jetbrains.exposed:exposed-core:$exposedVersion"
        const val dao = "org.jetbrains.exposed:exposed-dao:$exposedVersion"
        const val jdbc = "org.jetbrains.exposed:exposed-jdbc:$exposedVersion"
        const val kotlinDateTime = "org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion"

        const val javaTime = "org.jetbrains.exposed:exposed-java-time:$exposedVersion"
        const val apacheCommonText = "org.apache.commons:commons-text:1.8"

        const val postgresql = "org.postgresql:postgresql:42.3.1"
        const val h2 = "com.h2database:h2:1.4.199"
    }

    object Preferences {
        const val core = "com.russhwolf:multiplatform-settings:$preferencesVersion"
        const val coroutine = "com.russhwolf:multiplatform-settings-coroutines:$preferencesVersion"
        const val datastore = "com.russhwolf:multiplatform-settings-datastore:$preferencesVersion"
        const val datastoreX = "androidx.datastore:datastore-preferences:$datastoreXVersion"
        const val noArg = "com.russhwolf:multiplatform-settings-no-arg:$preferencesVersion"
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
        const val androidXCrypto = "androidx.security:security-crypto:$androidXCryptoVersion"
    }

    object Log {
        const val logback = "ch.qos.logback:logback-classic:$logbackVersion"
        const val kermit = "co.touchlab:kermit:$kermitVersion"
    }

    object DateTime {
        const val dateTimeX = "org.jetbrains.kotlinx:kotlinx-datetime:$dateTimeXVersion"
    }
}