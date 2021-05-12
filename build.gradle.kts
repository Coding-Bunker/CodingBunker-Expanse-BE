import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val koin_version: String by project
val exposed_version: String by project

val compileKotlin: KotlinCompile by tasks

plugins {
    application
    java
    idea
    kotlin("jvm") version "1.4.32"
    id("com.github.gmazzo.buildconfig") version "2.0.2"
    id("org.jmailen.kotlinter") version "3.3.0"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.4.32"
    kotlin("plugin.serialization") version "1.4.32"
}

// group = "it.codingbunker.tbs"
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
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
    maven { url = uri("https://kotlin.bintray.com/kotlin-js-wrappers") }
    maven { url = uri("https://kotlin.bintray.com/kotlinx") }
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
}

buildConfig {
    useKotlinOutput()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.1.1")

    implementation("io.ktor:ktor-server-tomcat:$ktor_version")

    implementation("ch.qos.logback:logback-classic:$logback_version")

    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-html-builder:$ktor_version")
    implementation("org.jetbrains:kotlin-css-jvm:1.0.0-pre.148-kotlin-1.4.30")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-locations:$ktor_version")
    implementation("io.ktor:ktor-metrics:$ktor_version")
    implementation("io.ktor:ktor-websockets:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")

    // KTOR Auth
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-okhttp:$ktor_version")
    implementation("io.ktor:ktor-client-serialization:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")

    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("io.ktor:ktor-client-mock:$ktor_version")
    testImplementation("io.ktor:ktor-client-mock-jvm:$ktor_version")

    implementation("org.koin:koin-ktor:$koin_version")
    implementation("org.koin:koin-logger-slf4j:$koin_version")
    testImplementation("org.koin:koin-test:$koin_version")

    // Result
    implementation("com.github.kittinunf.result:result:3.1.0")
    implementation("com.github.kittinunf.result:result-coroutines:3.1.0")

    // Exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("org.postgresql:postgresql:42.2.18")

    testImplementation("com.h2database:h2:1.4.199")

    implementation("org.apache.commons:commons-text:1.8")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:$kotlin_version")

    // crypt
    implementation("com.google.crypto.tink:tink:1.5.0")

    // RSocket
    implementation("io.rsocket.kotlin:rsocket-core:0.12.0")
    implementation("io.rsocket.kotlin:rsocket-transport-ktor:0.12.0")
    implementation("io.rsocket.kotlin:rsocket-transport-ktor-server:0.12.0")
}

configurations.all {
    // Exclude JUNIT4 Dedicated Kotlin Annotation
    exclude("org.jetbrains.kotlin", "kotlin-test-junit")
}

kotlin.sourceSets["main"].kotlin.srcDirs("src/main/kotlin")
kotlin.sourceSets["test"].kotlin.srcDirs("src/test/kotlin")

kotlin.sourceSets["main"].resources.srcDirs("src/main/resources")
kotlin.sourceSets["test"].resources.srcDirs("src/test/resources")

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_12.toString()
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.time.ExperimentalTime"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

kotlinter {
    disabledRules = arrayOf(
        "no-wildcard-imports"
    )
}