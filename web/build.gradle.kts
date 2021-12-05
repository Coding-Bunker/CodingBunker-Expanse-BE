plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.0.0"
}

version = "1.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(compose.web.widgets)
                implementation(compose.web.core)
                implementation(compose.runtime)
                implementation("com.arkivanov.decompose:decompose:0.4.0")
                implementation("com.arkivanov.mvikotlin:mvikotlin:3.0.0-alpha02")
                implementation("com.arkivanov.mvikotlin:mvikotlin-main:3.0.0-alpha02")
                implementation(npm("copy-webpack-plugin", "9.0.0"))
                implementation(npm("@material-ui/icons", "4.11.2"))
            }
        }
    }
}

// workaround for https://youtrack.jetbrains.com/issue/KT-48273
afterEvaluate {
    rootProject.extensions.configure<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension> {
        versions.webpackDevServer.version = "4.0.0"
        versions.webpackCli.version = "4.9.0"
    }
}

//compose.desktop {
//    application {
//        mainClass = ""
//    }
//}