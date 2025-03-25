package com.mooncloak.website.feature.billing

import androidx.compose.runtime.Immutable
import com.mooncloak.moonscape.snackbar.NotificationStateModel
import com.mooncloak.website.feature.billing.model.*

@Immutable
public data class BillingStateModel public constructor(
    public val billingCardPaymentUri: String = "https://mooncloak.com/billing/fiat",
    public val startDestination: BillingDestination = BillingDestination.Landing,
    public val selectedCryptoCurrency: SupportedCryptoCurrency = SupportedCryptoCurrency.Lunaris,
    public val cryptoCurrencies: Set<SupportedCryptoCurrency> = SupportedCryptoCurrency.orderedSet,
    public val invoices: Map<SupportedCryptoCurrency, CryptoInvoice?> = emptyMap(),
    public val paymentStatus: PlanPaymentStatus? = null,
    public val product: Plan? = null,
    public val token: String? = null,
    public val isLoading: Boolean = false,
    public val successMessage: NotificationStateModel? = null,
    public val errorMessage: NotificationStateModel? = null
)

public val BillingStateModel.invoice: CryptoInvoice?
    inline get() = invoices[selectedCryptoCurrency]
