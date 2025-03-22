plugins {
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("mooncloak.multiplatform")
    id("mooncloak.publish")
}

kotlin {
    sourceSets {
        all {
            // Disable warnings and errors related to these expected @OptIn annotations.
            // See: https://kotlinlang.org/docs/opt-in-requirements.html#module-wide-opt-in
            languageSettings.optIn("kotlin.RequiresOptIn")
            languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
            languageSettings.optIn("kotlinx.coroutines.FlowPreview")
            languageSettings.optIn("kotlin.time.ExperimentalTime")
            languageSettings.optIn("-Xexpect-actual-classes")
        }

        val commonMain by getting {
            dependencies {
                api(project(":feature-shared"))

                implementation(compose.components.resources)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmMain by getting {
            dependencies {
            }
        }

        val wasmJsMain by getting {
            dependencies {
                // Async Image Loading - coil
                // https://github.com/coil-kt/coil
                api("io.coil-kt.coil3:coil:_")
                api("io.coil-kt.coil3:coil-network-ktor3:_")
                implementation("io.coil-kt.coil3:coil-compose:_")

                // UI Shaders - HypnoticCanvas
                // https://github.com/mikepenz/HypnoticCanvas
                // Apache 2.0: https://github.com/mikepenz/HypnoticCanvas/blob/dev/LICENSE
                implementation("com.mikepenz.hypnoticcanvas:hypnoticcanvas:_")
            }
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.mooncloak.website.feature.download"
    generateResClass = always
}

compose.experimental {
    @Suppress("DEPRECATION")
    web.application {}
}
