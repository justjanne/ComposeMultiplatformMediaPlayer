pluginManagement {
    includeBuild("convention-plugins")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jogamp.org/deployment/maven/")
    }
}

rootProject.name = "compose-multiplatform-media-player"
include(":library", ":composeApp", ":iosApp")
