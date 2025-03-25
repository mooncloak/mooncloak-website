package com.mooncloak.website.feature.billing.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the request body for obtaining a [CryptoInvoice] model.
 *
 * @property [planId] The identifier of the [Plan] to request payment info about.
 *
 * @property [secret] A secret [String] value used to further check the status for the returned payment info
 * transaction. If this is provided, it MUST be provided for subsequent requests to obtain the status about the state
 * of a payment transaction for the returned payment info.
 */
@Immutable
@Serializable
public data class GetPaymentInvoiceRequestBody public constructor(
    @SerialName(value = "plan_id") public val planId: String,
    @SerialName(value = "secret") public val secret: String? = null,
    @SerialName(value = "currency_code") public val currencyCode: String? = null
)
