package com.mooncloak.website.feature.billing.model

import androidx.compose.runtime.Immutable
import com.mooncloak.kodetools.apix.core.ExperimentalApixApi
import com.mooncloak.kodetools.apix.core.HttpErrorBody
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
@OptIn(ExperimentalApixApi::class)
public data class BillingPaymentStatusDetails public constructor(
    @SerialName(value = "status") public val status: BillingPaymentStatus,
    @SerialName(value = "invoice_id") public val invoiceId: String? = null,
    @SerialName(value = "plan_id") public val planId: String? = null,
    @SerialName(value = "timestamp") public val timestamp: Instant? = null,
    @SerialName(value = "title") public val title: String? = null,
    @SerialName(value = "description") public val description: String? = null,
    @SerialName(value = "icon") public val icon: String? = null,
    @SerialName(value = "self") public val self: String? = null,
    @SerialName(value = "token") public val token: TransactionToken? = null,
    @SerialName(value = "error") public val error: HttpErrorBody<Nothing>? = null,
)
