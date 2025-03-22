package com.mooncloak.website.feature.shared.util

import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

public fun Route.installMooncloakWebsiteAssets() {
    // FIXME:
    // This is extremely annoying, but the client application, written in Kotlin/WASM, generates a JS file when
    // built. This JS file hardcodes the location to the WASM file for download. We have no way of overriding this.
    // And it expects that location to be at the root website location. So, we have to override the loading of a JS
    // file and redirect it to the CDN. I guess they only designed Kotlin/WASM for hello world applications and not
    // more complex and larger applications.
    get(Regex("(?<path>.*)\\.wasm")) {
        val path = this.call.parameters.getOrFail("path")
        val redirectPath = cdnAppPath("$path.wasm")

        this.call.respondRedirect(
            url = redirectPath,
            permanent = true
        )
    }

    // FIXME: Hardcoded resource path to vpn app.
    get("/composeResources/{path...}") {
        val path = this.call.parameters.getAll("path")?.joinToString(separator = "/")
        val redirectPath = cdnAppPath("/web-download/composeResources/$path")

        this.call.respondRedirect(
            url = redirectPath,
            permanent = true
        )
    }
}
