package com.mooncloak.website.feature.billing

import com.mooncloak.website.feature.shared.Feature

public val Feature.Companion.Billing: BillingFeature
    inline get() = BillingFeature

public expect object BillingFeature : BaseBillingFeature

public abstract class BaseBillingFeature internal constructor() : Feature {

    final override val packageId: String = "com.mooncloak.website.feature.billing"

    final override val pathPart: String = "feature-billing"

    final override val title: String = "Billing - mooncloak"

    final override val description: String =
        "Go Dark, Stay Bright with mooncloak VPN. Enjoy private, fast, secure browsing and shield yourself from data creeps. Try it now!"

    final override val keywords: List<String> = listOf("VPN", "app", "mooncloak", "no-logs VPN", "privacy", "download")

    final override val canvasElementId: String = "feature-billing"
}
