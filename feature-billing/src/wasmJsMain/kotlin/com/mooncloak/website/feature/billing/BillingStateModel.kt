package com.mooncloak.website.feature.billing

import androidx.compose.runtime.Immutable
import com.mooncloak.moonscape.snackbar.NotificationStateModel
import com.mooncloak.website.feature.billing.external.QueryParameters
import com.mooncloak.website.feature.billing.model.*

@Immutable
public data class BillingStateModel public constructor(
    public val queryParameters: QueryParameters = QueryParameters(),
    public val billingCardPaymentUri: String = "https://mooncloak.com/billing/fiat",
    public val redirectUri: String? = null,
    public val startDestination: BillingDestination = BillingDestination.Landing,
    public val selectedCryptoCurrency: CryptoCurrency = CryptoCurrency.orderedSet.first(),
    public val cryptoCurrencies: Set<CryptoCurrency> = CryptoCurrency.orderedSet,
    public val invoices: Map<CryptoCurrency, CryptoInvoice?> = emptyMap(),
    public val paymentStatusDetails: BillingPaymentStatusDetails? = null,
    public val selectedPlan: Plan? = null,
    public val plans: List<Plan> = emptyList(),
    public val token: String? = null,
    public val isLoading: Boolean = false,
    public val successMessage: NotificationStateModel? = null,
    public val errorMessage: NotificationStateModel? = null
)

public val BillingStateModel.invoice: CryptoInvoice?
    inline get() = invoices[selectedCryptoCurrency]
