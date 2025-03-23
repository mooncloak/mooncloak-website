package com.mooncloak.website.feature.download

import com.mooncloak.website.feature.shared.Feature

public expect object DownloadFeature : BaseDownloadFeature

public abstract class BaseDownloadFeature internal constructor() : Feature {

    final override val packageId: String = "com.mooncloak.website.feature.download"

    final override val pathPart: String = "feature-download"

    final override val title: String = "Download mooncloak"

    final override val canvasElementId: String = "feature-download"
}
