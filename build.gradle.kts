buildscript {
    val kotlin_version: String by project

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven(uri("https://plugins.gradle.org/m2/")) // For kotlinter-gradle
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlin_version}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${kotlin_version}")
        classpath("org.jetbrains.kotlin:kotlin-noarg:$kotlin_version")
    }
}