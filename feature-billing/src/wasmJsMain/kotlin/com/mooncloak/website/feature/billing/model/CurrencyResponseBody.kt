package com.mooncloak.website.feature.billing.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class CurrencyResponseBody public constructor(
    @SerialName(value = "currencies") public val currencies: List<Currency> = emptyList()
)
