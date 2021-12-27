plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlinx-serialization")
    id("com.rickclephas.kmp.nativecoroutines")
}

version = "unspecified"

repositories {
    mavenCentral()
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
        sourceSets["commonMain"].dependencies {

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

            with(Deps.KResult) {
                implementation(result)
            }

            with(Deps.Log) {
                api(kermit)
            }
        }

        sourceSets["androidMain"].dependencies {
//            implementation(Deps.Ktor.clientAndroid)
//            implementation(Deps.SqlDelight.androidDriver)
        }
    }
}