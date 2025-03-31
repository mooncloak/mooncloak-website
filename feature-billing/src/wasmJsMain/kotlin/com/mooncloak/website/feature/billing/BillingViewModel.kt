package com.mooncloak.website.feature.billing

import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.moonscape.snackbar.NotificationStateModel
import com.mooncloak.website.feature.billing.api.BillingApi
import com.mooncloak.website.feature.billing.external.*
import com.mooncloak.website.feature.billing.model.*
import kotlinx.browser.window
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.getString

public class BillingViewModel public constructor(
    private val billingApi: BillingApi,
    private val clock: Clock
) : ViewModel<BillingStateModel>(
    initialStateValue = BillingStateModel()
) {

    private val mutex = Mutex(locked = false)

    public fun load() {
        coroutineScope.launch {
            mutex.withLock {
                var selectedPlan: Plan? = null
                var token: TransactionToken? = null
                var invoice: CryptoInvoice? = null
                var paymentStatus: BillingPaymentStatus? = null
                var paymentStatusDetails: BillingPaymentStatusDetails? = null
                var queryParameters = QueryParameters()
                var plans = emptyList<Plan>()

                try {
                    emit { current -> current.copy(isLoading = true) }

                    queryParameters = URLSearchParams(window.location.search.toJsString()).parameters()

                    val productId = queryParameters["product_id"] ?: queryParameters["plan_id"]

                    token = queryParameters["token"]?.let { TransactionToken(value = it) }

                    paymentStatus = queryParameters["status"]?.let { BillingPaymentStatus(value = it) }

                    val productDeferred = async { getPlan(id = productId) }
                    val plansDeferred = async { getPlans() }

                    val currentState = state.current.value
                    val currency = currentState.selectedCryptoCurrency

                    invoice = currentState.invoices[currency]
                    paymentStatusDetails = currentState.paymentStatusDetails

                    val invoiceDeferred = async {
                        if (invoice == null && productId != null) {
                            invoice = getInvoice(
                                productId = productId,
                                token = currentState.token?.value,
                                currencyCode = currentState.selectedCryptoCurrency.currencyCode,
                                currentInvoices = currentState.invoices
                            )
                        }

                        invoice?.let {
                            billingApi.getPaymentStatus(token = it.token)
                        }
                    }

                    selectedPlan = productDeferred.await()
                    paymentStatusDetails = invoiceDeferred.await()
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
                            paymentStatusDetails = paymentStatusDetails,
                            queryParameters = queryParameters,
                            startDestination = when (paymentStatus) {
                                BillingPaymentStatus.Completed -> BillingDestination.Success
                                BillingPaymentStatus.Failed -> BillingDestination.Failed
                                else -> BillingDestination.Landing
                            }
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
                            paymentStatusDetails = paymentStatusDetails,
                            queryParameters = queryParameters,
                            startDestination = when (paymentStatus) {
                                BillingPaymentStatus.Completed -> BillingDestination.Success
                                BillingPaymentStatus.Failed -> BillingDestination.Failed
                                else -> BillingDestination.Landing
                            },
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
                            paymentStatusDetails = null
                        )
                    }

                    val currentState = state.current.value
                    val productId = currentState.selectedPlan?.id
                    var invoice = currentState.invoices[currency]

                    if (invoice == null && productId != null) {
                        invoice = getInvoice(
                            productId = productId,
                            token = currentState.token?.value,
                            currencyCode = currentState.selectedCryptoCurrency.currencyCode,
                            currentInvoices = currentState.invoices
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
                            paymentStatusDetails = paymentStatus
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

                    if (paymentStatus?.status == BillingPaymentStatus.Completed) {
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
                            paymentStatusDetails = paymentStatus,
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

    private suspend fun getPlans(): List<Plan> =
        billingApi.getPlans()
            .filter { plan -> plan.isAvailable(at = clock.now()) }
            .sortedBy { plan -> plan.price.amount }

    private suspend fun getPlan(id: String?): Plan? =
        id?.let { billingApi.getPlan(id = it) }

    private suspend fun getInvoice(
        currencyCode: Currency.Code,
        productId: String?,
        token: String?,
        currentInvoices: Map<CryptoCurrency, CryptoInvoice?>
    ): CryptoInvoice? {
        CryptoCurrency[currencyCode]?.let { currentInvoices[it] }?.let { return it }

        if (productId == null) return null

        return billingApi.getInvoice(
            productId = productId,
            token = token,
            currencyCode = currencyCode
        )
    }
}
