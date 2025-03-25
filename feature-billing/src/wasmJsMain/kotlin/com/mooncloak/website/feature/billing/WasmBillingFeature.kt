package com.mooncloak.website.feature.billing

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mooncloak.moonscape.theme.MooncloakTheme

public actual object BillingFeature : BaseBillingFeature() {

    @Composable
    override fun Content() {
        MooncloakTheme {
            BillingScreen(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
