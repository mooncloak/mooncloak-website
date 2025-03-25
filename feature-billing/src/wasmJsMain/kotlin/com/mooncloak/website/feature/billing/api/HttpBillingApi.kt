package com.mooncloak.website.feature.billing.api

import com.mooncloak.kodetools.apix.core.ExperimentalApixApi
import com.mooncloak.kodetools.apix.core.HttpResponseBody
import com.mooncloak.kodetools.apix.core.getOrThrow
import com.mooncloak.website.feature.billing.model.PaymentLink
import com.mooncloak.website.feature.billing.model.PaymentLinkResponseBody
import com.mooncloak.website.feature.billing.model.Plan
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalApixApi::class)
internal class HttpBillingApi internal constructor(
    private val httpClient: HttpClient,
    private val hostUrlProvider: HostUrlProvider
) : BillingApi {

    override suspend fun getProduct(id: String): Plan = withContext(Dispatchers.Default) {
        val response = httpClient.get(url("/billing/plan/$id"))

        return@withContext response.body<HttpResponseBody<Plan>>().getOrThrow()
    }

    override suspend fun getPaymentLinks(): List<PaymentLink> = withContext(Dispatchers.Default) {
        val response = httpClient.get(url("/billing/payment-links"))

        return@withContext response.body<HttpResponseBody<PaymentLinkResponseBody>>().getOrThrow().paymentLinks
    }

    private suspend fun url(vararg path: String, encodeSlash: Boolean = false): Url {
        val base = hostUrlProvider.get()

        return URLBuilder(base).apply {
            this.appendPathSegments(components = path, encodeSlash = encodeSlash)
        }.build()
    }
}
