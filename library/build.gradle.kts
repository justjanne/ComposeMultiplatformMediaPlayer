import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    id("module.publication")
}

kotlin {
    jvmToolchain(11)
    jvm()
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        all {
            languageSettings.optIn("androidx.compose.foundation.ExperimentalFoundationApi")
        }

        commonMain.dependencies {
            implementation(compose.foundation)
            implementation(compose.runtime)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)

            implementation(libs.image.loader)
            implementation(libs.compose.webview)
        }

        iosMain.dependencies {
        }

        jvmMain.dependencies {
            implementation(libs.vlcj)
            runtimeOnly(libs.vlcj.natives)
        }

        androidMain.dependencies {
            implementation(libs.androidx.core)
            implementation(libs.androidx.media.exoplayer.core)
            implementation(libs.androidx.media.ui)
            implementation(libs.androidx.media.exoplayer.hls)
            implementation(libs.androidx.lifecycle.process)
        }
    }
}

android {
    namespace = "ComposeMultiplatformMediaPlayer.org"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "reelsdemo.composemultiplatformmediaplayer.generated.resources"
    generateResClass = ResourcesExtension.ResourceClassGeneration.Always
}
