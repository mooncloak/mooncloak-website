package com.mooncloak.website.feature.shared.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.PopupPositionProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun MooncloakTooltipBox(
    text: String,
    modifier: Modifier = Modifier,
    positionProvider: PopupPositionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
    state: TooltipState = rememberTooltipState(),
    content: @Composable () -> Unit
) {
    androidx.compose.material3.TooltipBox(
        modifier = modifier,
        positionProvider = positionProvider,
        state = state,
        tooltip = {
            PlainTooltip {
                Text(text)
            }
        },
        content = content
    )
}
