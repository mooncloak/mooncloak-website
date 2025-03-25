pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
        maven("https://repo.repsy.io/mvn/mooncloak/public")
    }

    dependencyResolutionManagement {
        @Suppress("UnstableApiUsage")
        repositories {
            mavenCentral()
            google()
            maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
            maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
            maven("https://repo.repsy.io/mvn/mooncloak/public")
            maven("https://jitpack.io")
        }
    }

    includeBuild("build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"

    // See https://jmfayard.github.io/refreshVersions
    id("de.fayard.refreshVersions") version "0.60.5"

    // See build.gradle.kts file in root project folder for the rest of the plugins applied.
}

rootProject.name = "mooncloak-website"

include(":feature-shared")
include(":feature-download")
include(":feature-tip")
include(":feature-billing")
