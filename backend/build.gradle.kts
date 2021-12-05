val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val koin_version: String by project
val exposed_version: String by project
val kotlinx_datetime: String by project
val kotlin_css: String by project
val mockk_version: String by project

val webDir = file("src/frontendMain/web")

version = "0.0.1-alpha"

repositories {
    mavenCentral()
    maven { url = uri("https://repo1.maven.org/maven2") }
    google()
}

plugins {
    kotlin("jvm")
    application
//    id("org.jmailen.kotlinter") version "3.3.0"
    id("org.jetbrains.kotlin.plugin.noarg")
    kotlin("plugin.serialization")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_12.toString()
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.time.ExperimentalTime"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

//kotlinter {
//    disabledRules = arrayOf(
//        "no-wildcard-imports"
//    )
//}

dependencies {
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinx_datetime")

    implementation("io.ktor:ktor-server-tomcat:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-html-builder:$ktor_version")
//                implementation("org.jetbrains:kotlin-css-jvm:$kotlin_css")
    implementation("io.ktor:ktor-server-host-common:$ktor_version")
    implementation("io.ktor:ktor-locations:$ktor_version")
    implementation("io.ktor:ktor-metrics:$ktor_version")
    implementation("io.ktor:ktor-websockets:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    // KTOR Auth
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-serialization:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")
    implementation("io.ktor:ktor-client-logging:$ktor_version")
    implementation("io.ktor:ktor-client-apache:$ktor_version")

    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")

    // Result
    implementation("com.github.kittinunf.result:result:5.2.0")
    implementation("com.github.kittinunf.result:result-coroutines:4.0.0")
    // Exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("org.postgresql:postgresql:42.3.1")
    implementation("com.h2database:h2:1.4.199")
    implementation("org.apache.commons:commons-text:1.8")

    implementation("io.insert-koin:koin-core-ext:3.0.2")
    // crypt
    implementation("com.google.crypto.tink:tink:1.6.1")
    // RSocket
    implementation("io.rsocket.kotlin:rsocket-core:0.13.1")
    implementation("io.rsocket.kotlin:rsocket-transport-ktor:0.13.1")
    implementation("io.rsocket.kotlin:rsocket-transport-ktor-server:0.13.1")

    implementation("io.ktor:ktor-server-tests:$ktor_version")
    implementation("io.ktor:ktor-client-mock:$ktor_version")
    implementation("io.ktor:ktor-client-mock-jvm:$ktor_version")
    implementation("io.insert-koin:koin-test-junit5:$koin_version")
    implementation("io.insert-koin:koin-test:$koin_version")
    implementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    runtimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    implementation("org.jetbrains.kotlin:kotlin-test-junit5:$kotlin_version")
    implementation("io.mockk:mockk:$mockk_version")
    implementation("io.ktor:ktor-server-test-host:$ktor_version")

    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("io.ktor:ktor-client-mock:$ktor_version")
    testImplementation("io.ktor:ktor-client-mock-jvm:$ktor_version")
    testImplementation("io.insert-koin:koin-test-junit5:$koin_version")
    testImplementation("io.insert-koin:koin-test:$koin_version")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:$kotlin_version")
    testImplementation("io.mockk:mockk:$mockk_version")
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
}

configurations.all {
    // Exclude JUNIT4 Dedicated Kotlin Annotation
    exclude("org.jetbrains.kotlin", "kotlin-test-junit")
}