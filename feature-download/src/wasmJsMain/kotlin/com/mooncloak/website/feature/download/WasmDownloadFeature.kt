package com.mooncloak.website.feature.download

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mooncloak.moonscape.theme.MooncloakTheme
import com.mooncloak.website.feature.download.layout.DownloadLayout

public actual object DownloadFeature : BaseDownloadFeature() {

    @Composable
    override fun Content() {
        MooncloakTheme {
            DownloadLayout(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
