package com.mooncloak.website.feature.shared.page

import com.mooncloak.website.feature.shared.Feature

public data class FeaturePageItem public constructor(
    public val feature: Feature,
    public val path: String,
    public val title: String = feature.title,
    public val description: String? = feature.description,
    public val keywords: String? = null,
    public val cardImage: String? = null,
    public val headerVisible: Boolean = false,
    public val footerVisible: Boolean = false
)
