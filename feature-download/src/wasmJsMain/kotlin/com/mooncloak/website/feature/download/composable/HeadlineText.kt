package com.mooncloak.website.feature.download.composable

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mooncloak.moonscape.theme.MooncloakColorPalette

@Composable
internal fun HeadlineText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MooncloakColorPalette.MooncloakDarkPrimary
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.headlineLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 64.sp,
            lineHeight = 64.sp
        ),
        color = color
    )
}
