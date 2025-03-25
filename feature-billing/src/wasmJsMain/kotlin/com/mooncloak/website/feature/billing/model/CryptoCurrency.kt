package com.mooncloak.website.feature.billing.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CryptoCurrency public constructor(
    @SerialName(value = "name") public val name: String,
    @SerialName(value = "ticker") public val ticker: String? = null,
    @SerialName(value = "symbol") public val symbol: String? = null
)
