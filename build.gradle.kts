import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val koin_version: String by project
val kmongo_version: String by project
val compileKotlin: KotlinCompile by tasks

plugins {
    application
    java
    kotlin("jvm") version "1.4.10"
    id("com.github.gmazzo.buildconfig") version "2.0.2"
    id("org.jlleitschuh.gradle.ktlint") version "9.4.0"

//    id("koin") version "2.2.0-beta-1"
}

group = "it.codingbunker.tbs"
version = "0.0.1-alpha"

application {
    mainClassName = "io.ktor.server.tomcat.EngineMain"
}

repositories {
    mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
    maven { url = uri("https://kotlin.bintray.com/kotlin-js-wrappers") }
    maven { url = uri("https://kotlin.bintray.com/kotlinx") }
    maven { url = uri("https://plugins.gradle.org/m2/") }
}

buildConfig {
    useKotlinOutput()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("io.ktor:ktor-server-tomcat:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-html-builder:$ktor_version")
    implementation("org.jetbrains:kotlin-css-jvm:1.0.0-pre.31-kotlin-1.2.41")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-locations:$ktor_version")
    implementation("io.ktor:ktor-metrics:$ktor_version")
    implementation("io.ktor:ktor-websockets:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-core-jvm:$ktor_version")
//    implementation("io.ktor:ktor-client-http-timeout:$ktor_version")
    implementation("io.ktor:ktor-client-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-gson:$ktor_version")
    implementation("io.ktor:ktor-client-websockets:$ktor_version")
    implementation("io.ktor:ktor-client-logging-jvm:$ktor_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("io.ktor:ktor-client-mock:$ktor_version")
    testImplementation("io.ktor:ktor-client-mock-jvm:$ktor_version")

    implementation("org.koin:koin-ktor:$koin_version")
    implementation("org.koin:koin-logger-slf4j:$koin_version")
//    implementation("org.koin:koin-gradle-plugin:$koin_version")

    // KMongo
    implementation("org.litote.kmongo:kmongo:$kmongo_version")
    implementation("org.litote.kmongo:kmongo-coroutine:$kmongo_version")

    implementation("org.jlleitschuh.gradle:ktlint-gradle:9.4.0")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src/main")
kotlin.sourceSets["test"].kotlin.srcDirs("src/test")

sourceSets["main"].resources.srcDirs("src/main/resources")
sourceSets["test"].resources.srcDirs("src/test/resources")

compileKotlin.kotlinOptions {
    jvmTarget = JavaVersion.VERSION_12.toString()
}

ktlint {
    debug.set(true)
    verbose.set(true)
    outputToConsole.set(true)

    reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.CHECKSTYLE)
    }
}
