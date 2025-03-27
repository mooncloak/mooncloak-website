package com.mooncloak.website.feature.billing

import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.moonscape.snackbar.NotificationStateModel
import com.mooncloak.website.feature.billing.api.BillingApi
import com.mooncloak.website.feature.billing.crypto.CryptoChainlinkAddressProvider
import com.mooncloak.website.feature.billing.crypto.CryptoUSDPriceFetcher
import com.mooncloak.website.feature.billing.external.*
import com.mooncloak.website.feature.billing.model.*
import com.mooncloak.website.feature.billing.util.format
import kotlinx.browser.window
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.getString
import kotlin.math.pow
import kotlin.random.Random
import kotlin.time.Duration.Companion.minutes
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

public class BillingViewModel public constructor(
    private val billingApi: BillingApi,
    private val clock: Clock,
    private val priceFetcher: CryptoUSDPriceFetcher,
    private val addressProvider: CryptoChainlinkAddressProvider
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

                    val productDeferred = async { getPlan(id = productId) }
                    val plansDeferred = async { getPlans() }

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

                    loadInvoice(
                        currency = CryptoCurrency.POL,
                        plan = selectedPlan ?: plans.first()
                    )
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

    private suspend fun getPlans(): List<Plan> =
        billingApi.getPlans()
            .filter { plan -> plan.isAvailable(at = clock.now()) }
            .sortedBy { plan -> plan.price.amount }

    private suspend fun getPlan(id: String?): Plan? =
        id?.let { billingApi.getPlan(id = it) }

    @OptIn(ExperimentalUuidApi::class)
    private suspend fun loadInvoice(currency: CryptoCurrency, plan: Plan): CryptoInvoice =
        withContext(Dispatchers.Default) {
            // Ensure plan is available
            require(plan.isAvailable()) { "Plan '${plan.id}' is not available for purchase." }

            // Extract USD amount from plan.price (assumed in cents)
            val usdAmount = plan.price.amount / 100.0 // e.g., 800L -> 8.0

            // Fetch crypto price in USD
            val cryptoUsdPrice = priceFetcher.fetch(currency)
                ?: throw IllegalStateException("Could not fetch price for currency '$currency'.")

            // Calculate crypto amount with 10% buffer for gas
            val cryptoAmountRaw = (usdAmount / cryptoUsdPrice) * 1.10
            val decimals = when (currency) {
                CryptoCurrency.USDC, CryptoCurrency.Tether -> 6
                CryptoCurrency.POL, CryptoCurrency.Lunaris -> 18
            }
            val cryptoAmountScaled = (cryptoAmountRaw * 10.0.pow(decimals.toDouble())).toLong()

            // Crypto Currency model
            val cryptoCurrency = Currency(
                type = Currency.Type.Crypto,
                code = Currency.Code(currency.currencyCode)
            )

            // Prices
            val usdPrice = plan.price // Already in USD
            val cryptoPrice = Price(
                amount = cryptoAmountScaled,
                currency = cryptoCurrency
            )

            // Generate payment URI
            val weiAmount = cryptoAmountScaled.toString()
            val tokenAddress = addressProvider[currency]
                ?: throw IllegalArgumentException("No address found for '$currency'.")
            val uri =
                "ethereum:$CONTRACT_ADDRESS@137?value=$weiAmount&function=payWithToken(address token, uint256 amount)&address=$tokenAddress&uint256=$weiAmount"

            // Invoice fields
            val now = Clock.System.now()
            val token = TransactionToken(value = "txn-${Random.nextInt(1000000)}")

            CryptoInvoice(
                type = "crypto_payment",
                id = Uuid.random().toHexString(),
                planId = plan.id,
                token = token,
                created = now,
                expires = now + 15.minutes,
                uri = uri,
                amount = usdPrice,
                cryptoAmount = cryptoPrice,
                address = CONTRACT_ADDRESS,
                label = "Subscription Payment for plan ${plan.title}",
                message = "Pay ${plan.price.format()} in ${currency.name} for plan ${plan.title}",
                crypto = cryptoCurrency
            )
        }
}

// FIXME:
private const val CONTRACT_ADDRESS = "0xYourSubscriptionContractAddress"
