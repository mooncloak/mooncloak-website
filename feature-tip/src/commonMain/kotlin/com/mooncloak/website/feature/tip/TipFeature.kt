package com.mooncloak.website.feature.tip

import com.mooncloak.website.feature.shared.Feature

public val Feature.Companion.Tip: TipFeature
    inline get() = TipFeature

public expect object TipFeature : BaseTipFeature

public abstract class BaseTipFeature internal constructor() : Feature {

    final override val packageId: String = "com.mooncloak.website.feature.tip"

    final override val pathPart: String = "feature-tip"

    final override val title: String = "Thank Your Privacy Team!"

    final override val canvasElementId: String = "feature-tip"
}
