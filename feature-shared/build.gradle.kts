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
                // Coroutines
                // https://github.com/Kotlin/kotlinx.coroutines
                api(KotlinX.coroutines.core)

                // Serialization
                // https://github.com/Kotlin/kotlinx.serialization
                api(KotlinX.serialization.json)

                // Time
                // https://github.com/Kotlin/kotlinx-datetime
                api(KotlinX.datetime)

                // The resources and therefore the main runtime are needed in the common source set for some reason.
                api(compose.runtime)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val jvmMain by getting {
            dependencies {
                // HTML DSL - Ktor
                // https://ktor.io/docs/html-dsl.html#html_response
                api("io.ktor:ktor-server-html-builder:_")

                api("org.jetbrains.kotlinx:kotlinx-html:_")

                api("org.jetbrains.kotlin-wrappers:kotlin-css:_")

                // Bulma CSS kotlinx.html components
                // https://github.com/mooncloak/bulmak
                api("com.mooncloak.kodetools.bulmak:bulmak-core:_")
            }
        }

        val wasmJsMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.ui)
                api(compose.material3)

                // mooncloak themed UI - moonscape
                // https://github.com/mooncloak/moonscape
                api("com.mooncloak.moonscape:theme:_")
                api("com.mooncloak.moonscape:snackbar:_")
            }
        }
    }
}
