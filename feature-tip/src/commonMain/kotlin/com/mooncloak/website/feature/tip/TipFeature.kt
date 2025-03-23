package com.mooncloak.website.feature.tip

import com.mooncloak.website.feature.shared.Feature

public val Feature.Companion.Tip: TipFeature
    inline get() = TipFeature

public expect object TipFeature : BaseTipFeature

public abstract class BaseTipFeature internal constructor() : Feature {

    final override val packageId: String = "com.mooncloak.website.feature.tip"

    final override val pathPart: String = "feature-tip"

    final override val title: String = "Thank Your Privacy Team!"

    final override val description: String = "Give thanks to the team working tirelessly to protect your privacy! Every contribution helps fuel our mission."

    final override val keywords: List<String> = listOf("tip", "mooncloak", "vpn", "contributors", "team", "thank you", "privacy")

    final override val canvasElementId: String = "feature-tip"
}
