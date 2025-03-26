package com.mooncloak.website.feature.billing

import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.moonscape.snackbar.NotificationStateModel
import com.mooncloak.website.feature.billing.api.BillingApi
import com.mooncloak.website.feature.billing.model.*
import io.ktor.http.*
import kotlinx.browser.window
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.jetbrains.compose.resources.getString
import org.w3c.dom.url.URLSearchParams

public class BillingViewModel public constructor(
    private val billingApi: BillingApi
) : ViewModel<BillingStateModel>(
    initialStateValue = BillingStateModel()
) {

    private val mutex = Mutex(locked = false)

    public fun load() {
        coroutineScope.launch {
            mutex.withLock {
                var product: Plan? = null
                var token: String? = null
                var invoice: CryptoInvoice? = null
                var paymentStatus: PlanPaymentStatus? = null

                try {
                    emit { current -> current.copy(isLoading = true) }

                    val queryParameters = URLSearchParams(window.location.search.toJsString())
                    val productId = queryParameters.get("product_id")
                    token = queryParameters.get("token")

                    val productDeferred = async { productId?.let { billingApi.getProduct(id = it) } }

                    val currentState = state.current.value
                    val currency = currentState.selectedCryptoCurrency

                    invoice = currentState.invoices[currency]
                    paymentStatus = currentState.paymentStatus

                    val invoiceJob = async {
                        if (invoice == null && productId != null) {
                            invoice = billingApi.getInvoice(
                                productId = productId,
                                token = currentState.token,
                                currencyCode = currentState.selectedCryptoCurrency.currencyCode
                            )
                        }

                        paymentStatus = invoice?.let {
                            billingApi.getPaymentStatus(token = it.token)
                        }
                    }

                    product = productDeferred.await()
                    invoiceJob.await()

                    emit { current ->
                        current.copy(
                            isLoading = false,
                            product = product,
                            token = token,
                            invoices = current.invoices.toMutableMap()
                                .apply { this[currency] = invoice }
                                .toMap(),
                            paymentStatus = paymentStatus
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
                            product = product,
                            token = token,
                            paymentStatus = paymentStatus,
                            errorMessage = NotificationStateModel(message = getString(Res.string.error_loading_billing))
                        )
                    }
                }
            }
        }
    }

    public fun selectCurrency(currency: SupportedCryptoCurrency) {
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
                    val productId = currentState.product?.id
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
