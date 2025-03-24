package com.mooncloak.website.feature.download.model

import androidx.compose.runtime.Immutable

@Immutable
public data class HeadlineTextGroup public constructor(
    public val items: List<HeadlineTextItem> = emptyList()
) : ViewPageItem
