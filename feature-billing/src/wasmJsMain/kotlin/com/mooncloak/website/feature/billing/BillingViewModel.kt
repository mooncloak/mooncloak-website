package com.mooncloak.website.feature.billing

import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.error
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.moonscape.snackbar.NotificationStateModel
import com.mooncloak.website.feature.billing.api.BillingApi
import com.mooncloak.website.feature.billing.model.PaymentLink
import com.mooncloak.website.feature.billing.model.Plan
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
                var paymentLinks: List<PaymentLink> = emptyList()

                try {
                    emit { current -> current.copy(isLoading = true) }

                    val queryParameters = URLSearchParams(window.location.search.toJsString())
                    val productId = queryParameters.get("product_id")

                    val productDeferred = async { productId?.let { billingApi.getProduct(id = it) } }
                    paymentLinks = async { billingApi.getPaymentLinks() }.await()
                    product = productDeferred.await()

                    emit { current ->
                        current.copy(
                            isLoading = false,
                            product = product,
                            paymentLinks = paymentLinks
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
                            paymentLinks = paymentLinks,
                            errorMessage = NotificationStateModel(message = getString(Res.string.error_loading_billing))
                        )
                    }
                }
            }
        }
    }
}
