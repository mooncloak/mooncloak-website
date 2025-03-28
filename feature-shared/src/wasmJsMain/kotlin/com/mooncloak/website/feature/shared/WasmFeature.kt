package com.mooncloak.website.feature.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow

public actual interface Feature {

    public actual val packageId: String

    public actual val pathPart: String

    public actual val title: String

    public actual val description: String?

    public actual val keywords: List<String>

    public actual val canvasElementId: String

    @Composable
    public fun Content()

    public actual companion object
}

@OptIn(ExperimentalComposeUiApi::class)
public fun runFeature(feature: Feature) {
    CanvasBasedWindow(
        title = feature.title,
        canvasElementId = feature.canvasElementId
    ) {
        feature.Content()
    }
}
