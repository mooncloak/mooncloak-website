package com.mooncloak.website.feature.billing

import androidx.compose.runtime.Immutable
import com.mooncloak.moonscape.snackbar.NotificationStateModel
import com.mooncloak.website.feature.billing.model.PaymentLink
import com.mooncloak.website.feature.billing.model.Plan

@Immutable
public data class BillingStateModel public constructor(
    public val isLoading: Boolean = false,
    public val product: Plan? = null,
    public val paymentLinks: List<PaymentLink> = emptyList(),
    public val successMessage: NotificationStateModel? = null,
    public val errorMessage: NotificationStateModel? = null
)
