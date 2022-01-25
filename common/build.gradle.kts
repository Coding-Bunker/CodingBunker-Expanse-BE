import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
    id("com.rickclephas.kmp.nativecoroutines")
    id("com.github.gmazzo.buildconfig") version "3.0.3"
}

version = "unspecified"

val prop = Properties().apply {
    load(FileInputStream(File(rootProject.rootDir, "local.properties")))
}

println("Property:" + prop.getProperty("server.local.url"))

repositories {
    mavenCentral()
    maven("https://dl.bintray.com/florent37/maven")
}

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 26
        targetSdk = 31
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

//android {
//    configurations {
//        create("androidTestApi")
//        create("androidTestDebugApi")
//        create("androidTestReleaseApi")
//        create("testApi")
//        create("testDebugApi")
//        create("testReleaseApi")
//    }
//}

kotlin {
    android()
    jvm()

    sourceSets {
        sourceSets["commonMain"].apply {
            buildConfig {
                className("BuildConfigGenerated")
                packageName("it.github.codingbunker.tbs.common")

                buildConfigField("String", "SERVER_URL", "\"${prop.getProperty("server.local.url")}\"")
                buildConfigField("String", "SERVER_PORT", "\"${prop.getProperty("server.local.port")}\"")
            }

            dependencies {

                with(Deps.KtorClient) {
                    implementation(core)
                    implementation(serialization)
                    implementation(logging)
                    implementation(json)
                }

                with(Deps.Kotlinx) {
                    implementation(coroutinesCore)
                    implementation(serializationCore)
                }

                with(Deps.Koin) {
                    api(core)
                    api(test)
                }

                with(Deps.Preferences) {
                    implementation(core)
                    implementation(coroutine)
                }

                with(Deps.KResult) {
                    implementation(result)
                }

                with(Deps.Log) {
                    api(kermit)
                }
            }
        }

        sourceSets["androidMain"].dependencies {
            implementation(Deps.KtorClient.okhttp)
            implementation(Deps.Log.logback)
            implementation(Deps.Preferences.datastore)
            implementation(Deps.Preferences.datastoreX)
            with(Deps.Crypto) {
                implementation(androidXCrypto)
            }
//            implementation(Deps.SqlDelight.androidDriver)
        }
    }
}