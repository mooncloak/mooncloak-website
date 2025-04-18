package com.mooncloak.website.feature.billing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import com.mooncloak.moonscape.snackbar.NotificationStateModel
import com.mooncloak.website.feature.billing.external.QueryParameters
import com.mooncloak.website.feature.billing.model.*
import com.mooncloak.website.feature.billing.util.format

@Immutable
public data class BillingStateModel public constructor(
    public val queryParameters: QueryParameters = QueryParameters(),
    public val billingCardPaymentUri: String = "https://mooncloak.com/billing/payment",
    public val redirectUri: String? = null,
    public val startDestination: BillingDestination = BillingDestination.Landing,
    public val selectedCryptoCurrency: CryptoCurrency = CryptoCurrency.orderedSet.first(),
    public val cryptoCurrencies: Set<CryptoCurrency> = CryptoCurrency.orderedSet,
    public val invoices: Map<CryptoCurrency, CryptoInvoice?> = emptyMap(),
    public val paymentStatusDetails: BillingPaymentStatusDetails? = null,
    public val selectedPlan: Plan? = null,
    public val plans: List<Plan> = emptyList(),
    public val transactionToken: TransactionToken? = null,
    public val acceptedTerms: Boolean = false,
    public val termsAndConditionsText: @Composable () -> AnnotatedString = { AnnotatedString("") },
    public val noticeText: String? = null,
    public val isLoading: Boolean = false,
    public val successMessage: NotificationStateModel? = null,
    public val errorMessage: NotificationStateModel? = null
)

public val BillingStateModel.invoice: CryptoInvoice?
    inline get() = invoices[selectedCryptoCurrency]

public val BillingStateModel.priceText: String?
    inline get() {
        val invoice = this.invoice ?: return null
        val localFormatted = invoice.amount.format()
        val cryptoFormatted = invoice.cryptoAmount.format()

        return "$localFormatted - $cryptoFormatted"
    }

public val BillingStateModel.payButtonsEnabled: Boolean
    inline get() = this.acceptedTerms && this.selectedPlan != null
