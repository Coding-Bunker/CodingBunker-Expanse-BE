version = "0.0.1-alpha"

repositories {
    mavenCentral()
    maven { url = uri("https://repo1.maven.org/maven2") }
    google()
}

plugins {
    kotlin("jvm")
    application
    id("org.jmailen.kotlinter") version Versions.kotlinterVersion
    id("org.jetbrains.kotlin.plugin.noarg")
    kotlin("plugin.serialization")
}
tasks.named<Test>("test") {
    useJUnitPlatform()
}

kotlinter {
    disabledRules = arrayOf(
        "no-wildcard-imports"
    )
}

dependencies {
    implementation(kotlin("reflect"))

    with(Deps.Kotlinx) {
        implementation(kotlinJdk8)
        implementation(dateTimeX)
    }

    with(Deps.Ktor) {
        implementation(core)
        implementation(tomcat)
        implementation(htmlBuilder)
        implementation(serverHostCommon)
        implementation(locations)
        implementation(websocket)
        implementation(serialization)
        implementation(auth)
        implementation(session)

        implementation(metrics)
    }

    with(Deps.KtorClient) {
        implementation(core)
        implementation(serialization)
        implementation(logging)
        implementation(apache)
    }

    with(Deps.Log) {
        implementation(logback)
    }

    with(Deps.Koin) {
        implementation(ktor)
        implementation(logger)
        implementation(ext)
    }

    with(Deps.KResult) {
        implementation(result)
        implementation(coroutine)
    }

    with(Deps.Exposed) {
        implementation(core)
        implementation(dao)
        implementation(jdbc)
        implementation(javaTime)
        implementation(postgresql)
        implementation(apacheCommonText)

        testImplementation(h2)
    }

//    with(Deps.RSocket){
//        implementation(core)
//        implementation(ktor)
//        implementation(ktorServer)
//    }

    with(Deps.Test) {
        implementation(ktorServer)
        implementation(ktorServerHost)
        implementation(ktorClient)
        implementation(ktorClientJvm)
        implementation(koin)
        implementation(junit)
        implementation(junitEngine)
        implementation(kotlinJunit)
        implementation(mockk)
    }

    with(Deps.Crypto) {
        implementation(tink)
    }

    implementation(project(":common"))
}

configurations.all {
    // Exclude JUNIT4 Dedicated Kotlin Annotation
    exclude("org.jetbrains.kotlin", "kotlin-test-junit")
}