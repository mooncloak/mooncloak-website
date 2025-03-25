package com.mooncloak.website.feature.billing.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public data class ProofOfPurchase public constructor(
    @SerialName(value = "provider") public val paymentProvider: BillingProvider,
    @SerialName(value = "token") public val token: TransactionToken,
    @SerialName(value = "order_id") public val orderId: String? = null,
    @SerialName(value = "product_ids") public val productIds: List<String>? = null,
    @SerialName(value = "client_secret") public val clientSecret: String? = null,
    @SerialName(value = "subscription") public val subscription: Boolean = false
)
