plugins {
    kotlin("js")
//    id("org.jetbrains.compose") version "1.0.0"
}

version = "1.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers")
}

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
        binaries.executable()
    }
}

dependencies {
    implementation(kotlin("stdlib-js"))
//    implementation(compose.web.core)
//    implementation(compose.runtime)
//    implementation(compose.material)
//    implementation("com.arkivanov.decompose:decompose:0.4.0")
//    implementation("com.arkivanov.mvikotlin:mvikotlin:3.0.0-alpha02")
//    implementation("com.arkivanov.mvikotlin:mvikotlin-main:3.0.0-alpha02")
    implementation(npm("copy-webpack-plugin", "9.0.0"))

    implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.7.3")

    // https://mvnrepository.com/artifact/org.jetbrains.kotlin-wrappers/kotlin-react
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react:17.0.2-pre.277-kotlin-1.6.0")
    // https://mvnrepository.com/artifact/org.jetbrains.kotlin-wrappers/kotlin-react-dom
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:17.0.2-pre.277-kotlin-1.6.0")
    implementation(npm("react", "17.0.2"))
    implementation(npm("react-dom", "17.0.2"))
    implementation(npm("@mui/styled-engine-sc", "5.1.0"))
    implementation(npm("@emotion/react", "11.7.0"))
    implementation(npm("@emotion/styled", "11.6.0"))


    implementation("org.jetbrains.kotlin-wrappers:kotlin-styled:5.3.3-pre.264-kotlin-1.5.31")
    implementation(npm("styled-components", "~5.3.3"))

    // https://mvnrepository.com/artifact/org.jetbrains.kotlin-wrappers/kotlin-mui
    implementation("org.jetbrains.kotlin-wrappers:kotlin-mui:5.2.2-pre.278-kotlin-1.6.0")
    implementation(npm("@mui/material", "5.2.2"))
    // https://mvnrepository.com/artifact/org.jetbrains.kotlin-wrappers/kotlin-mui-icons
    implementation("org.jetbrains.kotlin-wrappers:kotlin-mui-icons:5.2.0-pre.278-kotlin-1.6.0")
    implementation(npm("@mui/icons-material", "5.2.0"))

//    implementation("com.ccfraser.muirwik:muirwik-components:0.9.1")

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