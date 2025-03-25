plugins {
    kotlin("plugin.serialization")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("mooncloak.multiplatform")
    id("mooncloak.publish")
    id("mooncloak.publishWebApp")
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
                implementation("com.mooncloak.kodetools.statex:statex-core:_")

                // Core API models: apix
                // https://github.com/mooncloak/apix
                // Apache 2.0: https://github.com/mooncloak/apix/blob/main/LICENSE
                implementation("com.mooncloak.kodetools.apix:apix-core:_")

                // Http Client - Ktor
                // https://github.com/ktorio/ktor
                api("io.ktor:ktor-client-core:_")
                api("io.ktor:ktor-client-content-negotiation:_")
                api("io.ktor:ktor-serialization-kotlinx-json:_")
                api("io.ktor:ktor-client-logging:_")
                implementation("io.ktor:ktor-client-encoding:_")

                // Rich Text Utils
                // https://github.com/mooncloak/textx
                // Apache 2.0: https://github.com/mooncloak/textx/blob/main/LICENSE
                api("com.mooncloak.kodetools.textx:textx-core:_")
            }
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.mooncloak.website.feature.billing"
    generateResClass = always
}

compose.experimental {
    @Suppress("DEPRECATION")
    web.application {}
}
