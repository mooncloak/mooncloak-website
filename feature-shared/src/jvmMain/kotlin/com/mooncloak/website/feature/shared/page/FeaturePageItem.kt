package com.mooncloak.website.feature.shared.page

import com.mooncloak.website.feature.shared.Feature

public data class FeaturePageItem public constructor(
    public val feature: Feature,
    public val path: String,
    public val title: String = feature.title,
    public val description: String? = feature.description,
    public val keywords: List<String> = feature.keywords,
    public val cardImage: String? = null,
    public val headerVisible: Boolean = false,
    public val footerVisible: Boolean = false
)

public fun Feature.toPageItem(
    path: String = this.pathPart,
    title: String = this.title,
    description: String? = this.description,
    keywords: List<String> = this.keywords,
    cardImage: String? = null,
    headerVisible: Boolean = false,
    footerVisible: Boolean = false
): FeaturePageItem = FeaturePageItem(
    feature = this,
    path = path,
    title = title,
    description = description,
    keywords = keywords,
    cardImage = cardImage,
    headerVisible = headerVisible,
    footerVisible = footerVisible
)
