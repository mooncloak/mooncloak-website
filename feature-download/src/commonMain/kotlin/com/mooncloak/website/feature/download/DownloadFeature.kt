package com.mooncloak.website.feature.download

import com.mooncloak.website.feature.shared.Feature

public val Feature.Companion.Download: DownloadFeature
    inline get() = DownloadFeature

public expect object DownloadFeature : BaseDownloadFeature

public abstract class BaseDownloadFeature internal constructor() : Feature {

    final override val packageId: String = "com.mooncloak.website.feature.download"

    final override val pathPart: String = "feature-download"

    final override val title: String = "Download mooncloak VPN - private, fast, & secure VPN service"

    final override val description: String =
        "Go Dark, Stay Bright with mooncloak VPN. Enjoy private, fast, secure browsing and shield yourself from data creeps. Try it now!"

    final override val keywords: List<String> = listOf("VPN", "app", "mooncloak", "no-logs VPN", "privacy", "download")

    final override val canvasElementId: String = "feature-download"
}
