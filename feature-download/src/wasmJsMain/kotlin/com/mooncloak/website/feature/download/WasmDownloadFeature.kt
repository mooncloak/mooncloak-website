package com.mooncloak.website.feature.download

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

public actual object DownloadFeature : BaseDownloadFeature() {

    @Composable
    override fun Content() {
        DownloadScreen(
            modifier = Modifier.fillMaxSize()
        )
    }
}
