package com.mooncloak.website.feature.shared

import kotlinx.html.FlowContent
import kotlinx.html.canvas
import kotlinx.html.id
import kotlinx.html.title

public actual interface Feature {

    public actual val packageId: String

    public actual val pathPart: String

    public actual val title: String

    public actual val canvasElementId: String

    public actual companion object
}

public fun FlowContent.installFeature(feature: Feature, classes: String? = null) {
    val formattedClasses = if (classes.isNullOrBlank()) {
        Feature.CLASS_NAME_MOONCLOAK_FEATURE
    } else {
        "${Feature.CLASS_NAME_MOONCLOAK_FEATURE} $classes"
    }

    canvas(classes = formattedClasses) {
        id = feature.canvasElementId
        title = feature.title
    }
}
