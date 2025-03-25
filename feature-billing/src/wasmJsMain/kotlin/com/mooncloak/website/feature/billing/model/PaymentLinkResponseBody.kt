package com.mooncloak.website.feature.billing.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class PaymentLinkResponseBody public constructor(
    @SerialName(value = "links") public val paymentLinks: List<PaymentLink> = emptyList()
)
