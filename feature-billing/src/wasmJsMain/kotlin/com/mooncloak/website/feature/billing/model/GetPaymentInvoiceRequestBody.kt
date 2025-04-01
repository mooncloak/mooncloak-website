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
 */
@Immutable
@Serializable
public data class GetPaymentInvoiceRequestBody public constructor(
    @SerialName(value = Key.PLAN_ID)
    @Deprecated(
        message = "Use productId",
        level = DeprecationLevel.WARNING
    )
    public val planId: String? = null,
    @SerialName(value = Key.PRODUCT_ID) public val productId: String? = planId,
    @SerialName(value = Key.CURRENCY_CODE) public val currencyCode: Currency.Code
) {

    public object Key {

        public const val PLAN_ID: String = "plan_id"
        public const val PRODUCT_ID: String = "product_id"
        public const val CURRENCY_CODE: String = "currency_code"
    }
}
