package com.mooncloak.website.feature.shared.util

import com.mooncloak.website.feature.shared.Feature
import com.mooncloak.website.feature.shared.path
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

public fun Route.installFeatureAssets() {
    // FIXME:
    // This is extremely annoying, but the client application, written in Kotlin/WASM, generates a JS file when
    // built. This JS file hardcodes the location to the WASM file for download. We have no way of overriding this.
    // And it expects that location to be at the root website location. So, we have to override the loading of a JS
    // file and redirect it to the CDN. I guess they only designed Kotlin/WASM for hello world applications and not
    // more complex and larger applications.
    //
    // This path will include the Feature pathPart, so we just have to delegate to the CDN that hosts the WASM files.
    get(Regex("(?<path>.*)\\.wasm")) {
        val path = this.call.parameters.getOrFail("path")
        val redirectPath = cdnAppPath("$path.wasm")

        this.call.respondRedirect(
            url = redirectPath,
            permanent = true
        )
    }
}

public fun Route.installFeatureResources(vararg feature: Feature) {
    this.installFeatureResources(features = feature.toList())
}

public fun Route.installFeatureResources(features: Collection<Feature>) {
    get("/composeResources/{path...}") {
        val path = this.call.parameters.getAll("path")?.joinToString(separator = "/") ?: ""

        val feature = features.firstOrNull { feature ->
            // The composeResources request will be located at `/composeResources/$packageId/$resourcePath`. For
            // example: `/composeResources/com.mooncloak.website.feature.download/values/strings.commonMain.cvr`.
            // So, we check if the path starts with the feature's packageId value, and fallback to checking the
            // feature's pathPart value. The first one that matches is considered the feature we will redirect to.
            path.startsWith(feature.packageId) || path.startsWith(feature.pathPart)
        }

        if (feature != null) {
            val redirectPath = feature.path("/composeResources/$path")

            this.call.respondRedirect(
                url = redirectPath,
                permanent = true
            )
        } else {
            this.call.respond(HttpStatusCode.NotFound)
        }
    }
}
