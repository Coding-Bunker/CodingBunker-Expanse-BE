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
    idea
    kotlin("jvm") version "1.4.10"
    id("com.github.gmazzo.buildconfig") version "2.0.2"
    id("org.jlleitschuh.gradle.ktlint") version "9.4.0"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.4.10"
//    id("koin") version "2.2.0-beta-1"
}

//group = "it.codingbunker.tbs"
version = "0.0.1-alpha"

application {
    mainClassName = "io.ktor.server.tomcat.EngineMain"
}

buildscript {

    repositories {
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }

    val kotlin_version: String by project
    val koin_version: String by project

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-noarg:$kotlin_version")
        classpath("org.koin:koin-gradle-plugin:$koin_version")
        classpath("org.jlleitschuh.gradle:ktlint-gradle:9.4.0")
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
    maven { url = uri("https://kotlin.bintray.com/kotlin-js-wrappers") }
    maven { url = uri("https://kotlin.bintray.com/kotlinx") }
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
    testImplementation("org.koin:koin-test:$koin_version")

    // KMongo
    implementation("org.litote.kmongo:kmongo:$kmongo_version")
    implementation("org.litote.kmongo:kmongo-coroutine:$kmongo_version")
    implementation("org.litote.kmongo:kmongo-id:$kmongo_version")
    implementation("org.litote.kmongo:kmongo-id-jackson:$kmongo_version")

    implementation("org.apache.commons:commons-text:1.8")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:$kotlin_version")
}

configurations.all {
    //Exclude JUNIT4 Dedicated Kotlin Annotation
    exclude("org.jetbrains.kotlin", "kotlin-test-junit")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src/main/kotlin")
kotlin.sourceSets["test"].kotlin.srcDirs("src/test/kotlin")

kotlin.sourceSets["main"].resources.srcDirs("src/main/resources")
kotlin.sourceSets["test"].resources.srcDirs("src/test/resources")

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_12.toString()
}

tasks.named<Test>("test") {
    useJUnitPlatform()
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
