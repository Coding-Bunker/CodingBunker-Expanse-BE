pluginManagement {
    repositories {
        maven { setUrl("https://plugins.gradle.org/m2/") }
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
}
rootProject.name = "expansebe"

include(":backend")
include(":web")