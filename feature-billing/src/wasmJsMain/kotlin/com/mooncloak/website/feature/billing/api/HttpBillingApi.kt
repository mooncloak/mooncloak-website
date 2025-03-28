package com.mooncloak.website.feature.billing.api

import com.mooncloak.kodetools.apix.core.ExperimentalApixApi
import com.mooncloak.kodetools.apix.core.HttpResponseBody
import com.mooncloak.kodetools.apix.core.getOrThrow
import com.mooncloak.website.feature.billing.model.*
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

    override suspend fun getSupportedCryptoCurrencies(): List<Currency> = withContext(Dispatchers.Default) {
        val response = httpClient.get(url("/billing/currencies")) {
            parameter("type", Currency.Type.Crypto.value)
        }

        return@withContext response.body<HttpResponseBody<CurrencyResponseBody>>().getOrThrow().currencies
    }

    override suspend fun getPlan(id: String): Plan = withContext(Dispatchers.Default) {
        val response = httpClient.get(url("/billing/plan/$id"))

        return@withContext response.body<HttpResponseBody<Plan>>().getOrThrow()
    }

    override suspend fun getPlans(): List<Plan> =
        withContext(Dispatchers.Default) {
            val response = httpClient.get(url("/billing/plans"))

            return@withContext response.body<HttpResponseBody<AvailablePlans>>().getOrThrow().plans
        }

    override suspend fun getInvoice(productId: String, token: String?, currencyCode: Currency.Code): CryptoInvoice =
        withContext(Dispatchers.Default) {
            val response = httpClient.post(url("/billing/payment/invoice")) {
                token?.let { bearerAuth(it) }

                contentType(ContentType.Application.Json)
                accept(ContentType.Application.Json)

                setBody(
                    GetPaymentInvoiceRequestBody(
                        planId = productId,
                        currencyCode = currencyCode
                    )
                )
            }

            return@withContext response.body<HttpResponseBody<CryptoInvoice>>().getOrThrow()
        }

    override suspend fun getPaymentStatus(
        token: TransactionToken
    ): BillingPaymentStatusDetails = withContext(Dispatchers.Default) {
        val response = httpClient.get(url("/billing/payment/status")) {
            bearerAuth(token.value)

            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }

        return@withContext response.body<HttpResponseBody<BillingPaymentStatusDetails>>().getOrThrow()
    }

    private suspend fun url(vararg path: String, encodeSlash: Boolean = false): Url {
        val base = hostUrlProvider.get()

        return URLBuilder(base).apply {
            this.appendPathSegments(components = path, encodeSlash = encodeSlash)
        }.build()
    }
}
