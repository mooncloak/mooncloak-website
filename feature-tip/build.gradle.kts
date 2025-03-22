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
                implementation(compose.components.resources)
            }
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.mooncloak.website.feature.tip"
    generateResClass = always
}

compose.experimental {
    @Suppress("DEPRECATION")
    web.application {}
}
