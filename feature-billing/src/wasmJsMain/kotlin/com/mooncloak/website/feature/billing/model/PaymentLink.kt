package com.mooncloak.website.feature.billing.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class PaymentLink public constructor(
    @SerialName(value = "url") public val url: String,
    @SerialName(value = "title") public val title: String,
    @SerialName(value = "description") public val description: String? = null,
    @SerialName(value = "icon") public val icon: String? = null
)
