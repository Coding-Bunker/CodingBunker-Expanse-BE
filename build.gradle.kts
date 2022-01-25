buildscript {

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(uri("https://plugins.gradle.org/m2/")) // For kotlinter-gradle
        maven("https://mvnrepository.com/artifact/com.android.tools.lint/lint-gradle-api")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlinVersion}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlinVersion}")
        classpath("org.jetbrains.kotlin:kotlin-noarg:${Versions.kotlinVersion}")

        val kmpNativeCoroutinesVersion = if (Versions.kotlinVersion == "1.6.10") "0.10.0-new-mm" else "0.8.0"
        classpath("com.rickclephas.kmp:kmp-nativecoroutines-gradle-plugin:$kmpNativeCoroutinesVersion")

        with(Deps.Gradle) {
            classpath(gradleVersionsPlugin)
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_12.toString()
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.time.ExperimentalTime"
}

tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask>().configureEach {
    revision = "release"
}

allprojects {
    apply(plugin = "com.github.ben-manes.versions")

    repositories {
        google()
        mavenCentral()
    }
}