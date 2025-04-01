package com.mooncloak.website.feature.billing

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.style.TextDecoration
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
                var paymentStatus: BillingPaymentStatus? = null
                var queryParameters = QueryParameters()
                var plans = emptyList<Plan>()
                var termsAndConditionsText: (@Composable () -> AnnotatedString) = { AnnotatedString("") }

                try {
                    emit { current -> current.copy(isLoading = true) }

                    queryParameters = URLSearchParams(window.location.search.toJsString()).parameters()

                    val productId = queryParameters["product_id"] ?: queryParameters["plan_id"]

                    token = queryParameters["token"]?.let { TransactionToken(value = it) }

                    paymentStatus = queryParameters["status"]?.let { BillingPaymentStatus(value = it) }

                    termsAndConditionsText = getTermsAndConditionsText()

                    val plansDeferred = async { getPlans() }

                    plans = plansDeferred.await()
                    selectedPlan = plans.firstOrNull { plan -> plan.id == productId }

                    emit { current ->
                        current.copy(
                            isLoading = false,
                            selectedPlan = selectedPlan,
                            plans = plans,
                            token = token,
                            queryParameters = queryParameters,
                            startDestination = when (paymentStatus) {
                                BillingPaymentStatus.Completed -> BillingDestination.Success
                                BillingPaymentStatus.Failed -> BillingDestination.Failed
                                else -> BillingDestination.Landing
                            },
                            termsAndConditionsText = termsAndConditionsText
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
                            queryParameters = queryParameters,
                            startDestination = when (paymentStatus) {
                                BillingPaymentStatus.Completed -> BillingDestination.Success
                                BillingPaymentStatus.Failed -> BillingDestination.Failed
                                else -> BillingDestination.Landing
                            },
                            termsAndConditionsText = termsAndConditionsText,
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

    public fun loadInvoice() {
        coroutineScope.launch {
            mutex.withLock {
                try {
                    val currentState = state.current.value
                    val productId = currentState.selectedPlan?.id
                    val currency = currentState.selectedCryptoCurrency
                    var invoice = currentState.invoice

                    if (invoice == null) {
                        invoice = getInvoice(
                            productId = productId,
                            currencyCode = currentState.selectedCryptoCurrency.currencyCode,
                            currentInvoices = currentState.invoices
                        )
                    }

                    invoice?.let {
                        billingApi.getPaymentStatus(token = it.token)
                    }

                    emit { current ->
                        current.copy(
                            invoices = current.invoices.toMutableMap()
                                .apply { this[currency] = invoice }
                                .toMap(),
                        )
                    }
                } catch (e: Exception) {
                    LogPile.error(
                        message = "Error fetching invoice.",
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

    public fun toggleAcceptTerms(accepted: Boolean) {
        coroutineScope.launch {
            mutex.withLock {
                emit { current ->
                    current.copy(
                        acceptedTerms = accepted
                    )
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
        currentInvoices: Map<CryptoCurrency, CryptoInvoice?>
    ): CryptoInvoice? {
        CryptoCurrency[currencyCode]?.let { currentInvoices[it] }?.let { return it }

        if (productId == null) return null

        return billingApi.getInvoice(
            productId = productId,
            currencyCode = currencyCode
        )
    }

    private suspend fun getTermsAndConditionsText(): (@Composable () -> AnnotatedString) {
        val acceptTermsAndConditionsText = getString(Res.string.payment_accept_terms_and_conditions)
        val termsLinkText = getString(Res.string.payment_link_text_terms)
        val privacyPolicyLinkText = getString(Res.string.payment_link_text_privacy_policy)
        val indexOfTerms = acceptTermsAndConditionsText.indexOf(termsLinkText)
        val indexOfPrivacyPolicy = acceptTermsAndConditionsText.indexOf(privacyPolicyLinkText)

        return {
            AnnotatedString.Builder().apply {
                append(acceptTermsAndConditionsText)

                if (indexOfTerms != -1) {
                    addLink(
                        url = LinkAnnotation.Url(
                            url = "https://mooncloak.com/legal/terms",
                            styles = TextLinkStyles(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline
                                ),
                                pressedStyle = SpanStyle(
                                    color = MaterialTheme.colorScheme.tertiaryContainer,
                                    textDecoration = TextDecoration.Underline
                                )
                            )
                        ),
                        start = indexOfTerms,
                        end = indexOfTerms + termsLinkText.length
                    )
                }

                if (indexOfPrivacyPolicy != -1) {
                    addLink(
                        url = LinkAnnotation.Url(
                            url = "https://mooncloak.com/legal/privacy",
                            styles = TextLinkStyles(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    textDecoration = TextDecoration.Underline
                                ),
                                pressedStyle = SpanStyle(
                                    color = MaterialTheme.colorScheme.tertiaryContainer,
                                    textDecoration = TextDecoration.Underline
                                )
                            )
                        ),
                        start = indexOfPrivacyPolicy,
                        end = indexOfPrivacyPolicy + privacyPolicyLinkText.length
                    )
                }
            }.toAnnotatedString()
        }
    }
}
