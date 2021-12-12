buildscript {
    val kotlin_version: String by project

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(uri("https://plugins.gradle.org/m2/")) // For kotlinter-gradle
        maven("https://mvnrepository.com/artifact/com.android.tools.lint/lint-gradle-api")
    }

    dependencies {
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlin_version}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${kotlin_version}")
        classpath("org.jetbrains.kotlin:kotlin-noarg:$kotlin_version")
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_12.toString()
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.time.ExperimentalTime"
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}