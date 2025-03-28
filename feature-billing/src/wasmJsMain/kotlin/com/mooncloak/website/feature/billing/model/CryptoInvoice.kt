package com.mooncloak.website.feature.billing.model

import androidx.compose.runtime.Immutable
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents information about how to pay for a plan.
 *
 * @property [type] The type of this invoice model.
 *
 * @property [id] An opaque, unique identifier value for this plan payment invoice, used to associate this
 * information to a particular transaction when purchasing.
 *
 * @property [planId] The unique identifier value for the [Plan] that this invoice is for.
 *
 * @property [token] An opaque unique token [String] value generated for the transaction associated with this payment
 * info instance. If the user purchases the plan, via the properties on this model, then the user can obtain the status
 * of the purchase using this token for authorization.
 *
 * @property [created] The [Instant] that this model was first created.
 *
 * @property [expires] The [Instant] that this invoice expires and is no longer valid.
 *
 * @property [paymentUri] The URI [String] to open a wallet to pay for the plan. For instance, this would be a BIP 21 URI for
 * Bitcoin.
 *
 * @property [amount] The [Price] model representing the amount required to complete this transaction in the local
 * currency.
 *
 * @property [cryptoAmount] The [Price] model representing the amount required to complete this transaction in the
 * cryptocurrency.
 *
 * @property [address] The wallet address for a crypto payment.
 *
 * @property [label] A label to identify the recipient or purpose of the payment.
 *
 * @property [message] A message to display to the user, providing additional information about the payment.
 *
 * @property [crypto] The crypto [Currency] model for the cryptocurrency for this invoice.
 */
@Immutable
@Serializable
public data class CryptoInvoice public constructor(
    @SerialName(value = "type") public val type: String,
    @SerialName(value = "id") public val id: String,
    @SerialName(value = "product_id") public val planId: String,
    @SerialName(value = "token") public val token: TransactionToken,
    @SerialName(value = "created") public val created: Instant,
    @SerialName(value = "expires") public val expires: Instant? = null,
    @SerialName(value = "payment_uri") public val paymentUri: String,
    @SerialName(value = "amount") public val amount: Price,
    @SerialName(value = "crypto_amount") public val cryptoAmount: Price,
    @SerialName(value = "address") public val address: String,
    @SerialName(value = "label") public val label: String? = null,
    @SerialName(value = "message") public val message: String? = null,
    @SerialName(value = "crypto") public val crypto: Currency? = null
)
