package com.mooncloak.website.feature.tip

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mooncloak.moonscape.theme.MooncloakTheme

public actual object TipFeature : BaseTipFeature() {

    @Composable
    override fun Content() {
        MooncloakTheme {
            TipScreen(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
