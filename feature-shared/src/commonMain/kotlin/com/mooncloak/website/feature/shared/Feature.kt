package com.mooncloak.website.feature.shared

public expect interface Feature {

    public val title: String

    public val canvasElementId: String

    public companion object
}

public val Feature.Companion.BASE_CDN_PATH: String
    get() = BASE_CDN_PATH_SINGLETON

public val Feature.Companion.BASE_CDN_APP_PATH: String
    get() = BASE_CDN_APP_PATH_SINGLETON

public val Feature.Companion.CLASS_NAME_MOONCLOAK_FEATURE: String
    get() = CLASS_NAME_MOONCLOAK_FEATURE_SINGLETON

private const val BASE_CDN_PATH_SINGLETON: String = "https://cdn.mooncloak.com"
private const val BASE_CDN_APP_PATH_SINGLETON: String = "https://cdn.mooncloak.com/app/latest/web"
private const val CLASS_NAME_MOONCLOAK_FEATURE_SINGLETON: String = "mooncloak-feature"
