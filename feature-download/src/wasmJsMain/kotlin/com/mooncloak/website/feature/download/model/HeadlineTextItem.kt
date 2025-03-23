package com.mooncloak.website.feature.download.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
public data class HeadlineTextItem public constructor(
    public val text: String,
    public val color: Color = Color.White
)
