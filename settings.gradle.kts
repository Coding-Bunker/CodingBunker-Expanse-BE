rootProject.name = "expansebe"

pluginManagement {
    val kotlin_version: String by settings

    plugins {
        kotlin("jvm") version kotlin_version
        id("org.jetbrains.kotlin.plugin.noarg") version kotlin_version
        kotlin("plugin.serialization") version kotlin_version
    }
}