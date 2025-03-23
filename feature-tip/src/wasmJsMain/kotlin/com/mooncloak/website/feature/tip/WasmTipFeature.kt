package com.mooncloak.website.feature.tip

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

public actual object TipFeature : BaseTipFeature() {

    @Composable
    override fun Content() {
        TipScreen(
            modifier = Modifier.fillMaxSize()
        )
    }
}
