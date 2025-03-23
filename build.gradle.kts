plugins {
    kotlin("jvm") version "2.1.10" apply false
    kotlin("multiplatform") version "2.1.10" apply false
    kotlin("android") version "2.1.10" apply false
    kotlin("plugin.serialization") version "2.1.10" apply false
    id("com.android.application") version "8.7.3" apply false
    id("com.android.library") version "8.7.3" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.10" apply false
    id("org.jetbrains.compose") version "1.7.3" apply false
    id("com.google.devtools.ksp") apply false
    id("mooncloak.variables")
}

group = buildVariables.group
version = buildVariables.version

// A task that publishes the maven artifacts and the web app binaries for all the feature modules.
//
// NOTE: Each feature module must have the "mooncloak.publish" and "mooncloak.publishWebApp" Gradle plugins installed.
tasks.register("release") {
    group = "mooncloak"
    description = "Publishes the maven artifacts and the web app binaries."

    val subProjectPublishTasks = subprojects.filter { subproject ->
        subproject.plugins.hasPlugin("mooncloak.publish")
    }.map { subproject -> subproject.tasks.named("publish") }

    dependsOn(subProjectPublishTasks)

    val subProjectPublishWebAppTasks = subprojects.filter { subproject ->
        subproject.plugins.hasPlugin("mooncloak.publishWebApp")
    }.map { subproject -> subproject.tasks.named("publishWebApp") }

    dependsOn(subProjectPublishWebAppTasks)
}
