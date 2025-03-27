package com.mooncloak.website.feature.billing

import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.moonscape.snackbar.NotificationStateModel
import com.mooncloak.website.feature.billing.api.BillingApi
import com.mooncloak.website.feature.billing.external.QueryParameters
import com.mooncloak.website.feature.billing.external.URLSearchParams
import com.mooncloak.website.feature.billing.external.get
import com.mooncloak.website.feature.billing.external.parameters
import com.mooncloak.website.feature.billing.model.*
import kotlinx.browser.window
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.compose.resources.getString

public class BillingViewModel public constructor(
    private val billingApi: BillingApi
) : ViewModel<BillingStateModel>(
    initialStateValue = BillingStateModel()
) {

    private val mutex = Mutex(locked = false)

    public fun load() {
        coroutineScope.launch {
            mutex.withLock {
                var selectedPlan: Plan? = null
                var token: String? = null
                var invoice: CryptoInvoice? = null
                var paymentStatus: PlanPaymentStatus? = null
                var queryParameters = QueryParameters()
                var plans = emptyList<Plan>()

                try {
                    emit { current -> current.copy(isLoading = true) }

                    queryParameters = URLSearchParams(window.location.search.toJsString()).parameters()

                    val productId = queryParameters["product_id"] ?: queryParameters["plan_id"]

                    token = queryParameters["token"]

                    val productDeferred = async { productId?.let { billingApi.getPlan(id = it) } }
                    val plansDeferred = async { billingApi.getPlans().sortedBy { it.price.amount } }

                    val currentState = state.current.value
                    val currency = currentState.selectedCryptoCurrency

                    invoice = currentState.invoices[currency]
                    paymentStatus = currentState.paymentStatus

                    val invoiceDeferred = async {
                        if (invoice == null && productId != null) {
                            invoice = billingApi.getInvoice(
                                productId = productId,
                                token = currentState.token,
                                currencyCode = currentState.selectedCryptoCurrency.currencyCode
                            )
                        }

                        invoice?.let {
                            billingApi.getPaymentStatus(token = it.token)
                        }
                    }

                    selectedPlan = productDeferred.await()
                    paymentStatus = invoiceDeferred.await()
                    plans = plansDeferred.await()

                    emit { current ->
                        current.copy(
                            isLoading = false,
                            selectedPlan = selectedPlan,
                            plans = plans,
                            token = token,
                            invoices = current.invoices.toMutableMap()
                                .apply { this[currency] = invoice }
                                .toMap(),
                            paymentStatus = paymentStatus,
                            queryParameters = queryParameters
                        )
                    }
                } catch (e: Exception) {
                    LogPile.error(
                        message = "Error retrieving billing information.",
                        cause = e
                    )

                    emit { current ->
                        current.copy(
                            isLoading = false,
                            selectedPlan = selectedPlan,
                            plans = plans,
                            token = token,
                            paymentStatus = paymentStatus,
                            queryParameters = queryParameters,
                            errorMessage = NotificationStateModel(message = getString(Res.string.error_loading_billing))
                        )
                    }
                }
            }
        }
    }

    public fun selectPlan(plan: Plan?) {
        coroutineScope.launch {
            mutex.withLock {
                emit { current ->
                    current.copy(
                        selectedPlan = plan
                    )
                }
            }
        }
    }

    public fun selectCurrency(currency: CryptoCurrency) {
        coroutineScope.launch {
            mutex.withLock {
                try {
                    emit { current ->
                        current.copy(
                            selectedCryptoCurrency = currency,
                            paymentStatus = null
                        )
                    }

                    val currentState = state.current.value
                    val productId = currentState.selectedPlan?.id
                    var invoice = currentState.invoices[currency]

                    if (invoice == null && productId != null) {
                        invoice = billingApi.getInvoice(
                            productId = productId,
                            token = currentState.token,
                            currencyCode = currentState.selectedCryptoCurrency.currencyCode
                        )
                    }

                    val paymentStatus = invoice?.let {
                        billingApi.getPaymentStatus(token = invoice.token)
                    }

                    emit { current ->
                        current.copy(
                            isLoading = false,
                            invoices = current.invoices.toMutableMap()
                                .apply { this[currency] = invoice }
                                .toMap(),
                            paymentStatus = paymentStatus
                        )
                    }
                } catch (e: Exception) {
                    LogPile.error(
                        message = "Error loading crypto details.",
                        cause = e
                    )

                    emit { current ->
                        current.copy(
                            isLoading = false,
                            errorMessage = NotificationStateModel(message = getString(Res.string.error_loading_crypto_details))
                        )
                    }
                }
            }
        }
    }

    public fun refreshStatus() {
        coroutineScope.launch {
            mutex.withLock {
                try {
                    val currentState = state.current.value
                    val currency = currentState.selectedCryptoCurrency
                    val invoice = currentState.invoice
                    val paymentStatus = invoice?.let {
                        billingApi.getPaymentStatus(token = invoice.token)
                    }
                    var redirectUri: String? = null

                    if (paymentStatus is PlanPaymentStatus.Completed) {
                        val queryString = window.location.search
                        val token = paymentStatus.token?.value
                        redirectUri = buildString {
                            append("https://mooncloak.com/payment/result")

                            if (token != null && queryString.isNotBlank()) {
                                append("?")
                            }

                            if (token != null) {
                                append("token=${token}")
                            }

                            if (queryString.isNotBlank()) {
                                if (token != null) {
                                    append("&")
                                }

                                append(queryString.removePrefix("?"))
                            }
                        }
                    }

                    emit { current ->
                        current.copy(
                            isLoading = false,
                            invoices = current.invoices.toMutableMap()
                                .apply { this[currency] = invoice }
                                .toMap(),
                            paymentStatus = paymentStatus,
                            redirectUri = redirectUri
                        )
                    }
                } catch (e: Exception) {
                    LogPile.error(
                        message = "Error refreshing status",
                        cause = e
                    )

                    emit { current ->
                        current.copy(
                            isLoading = false,
                            errorMessage = NotificationStateModel(message = getString(Res.string.error_refreshing_status))
                        )
                    }
                }
            }
        }
    }
}
