package com.mooncloak.website.feature.shared

public expect interface Feature {

    // ex: com.mooncloak.website.feature.download
    public val packageId: String

    // ex: feature-download
    public val pathPart: String

    // ex: Download the mooncloak VPN app!
    public val title: String

    // ex: feature-download
    public val canvasElementId: String

    public companion object
}

public fun Feature.path(vararg parts: String): String =
    if (parts.isEmpty()) {
        Feature.BASE_CDN_APP_PATH
    } else {
        val path = parts.joinToString(separator = "/") { part ->
            part.removePrefix("/")
        }
        val formattedFeaturePathPart = this.pathPart.removePrefix("/")

        "${Feature.BASE_CDN_APP_PATH}/$formattedFeaturePathPart/$path"
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
