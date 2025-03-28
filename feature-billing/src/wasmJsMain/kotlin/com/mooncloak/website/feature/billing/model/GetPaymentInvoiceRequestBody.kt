package com.mooncloak.website.feature.billing.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the request body for obtaining a [CryptoInvoice] model.
 *
 * @property [planId] The identifier of the [Plan] to request payment info about.
 *
 * @property [currencyCode] The requested [Currency.Code] for the invoice.
 *
 * @property [lunarisAddress] The public lunaris address to send gift Lunaris tokens to upon completed purchase.
 */
@Immutable
@Serializable
public data class GetPaymentInvoiceRequestBody public constructor(
    @SerialName(value = "plan_id")
    @Deprecated(
        message = "Use productId",
        level = DeprecationLevel.WARNING
    )
    public val planId: String,
    @SerialName(value = "product_id") public val productId: String = planId,
    @SerialName(value = "currency_code") public val currencyCode: Currency.Code,
    @SerialName(value = "lunaris_address") public val lunarisAddress: String? = null
)
