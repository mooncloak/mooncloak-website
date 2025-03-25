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

    override suspend fun getProduct(id: String): Plan = withContext(Dispatchers.Default) {
        val response = httpClient.get(url("/billing/plan/$id"))

        return@withContext response.body<HttpResponseBody<Plan>>().getOrThrow()
    }

    override suspend fun getInvoice(productId: String, token: String?, currencyCode: String): CryptoInvoice =
        withContext(Dispatchers.Default) {
            val response = httpClient.post(url("/billing/invoice")) {
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
    ): PlanPaymentStatus = withContext(Dispatchers.Default) {
        val response = httpClient.get(url("/billing/status")) {
            bearerAuth(token.value)

            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
        }

        return@withContext response.body<HttpResponseBody<PlanPaymentStatus>>().getOrThrow()
    }

    private suspend fun url(vararg path: String, encodeSlash: Boolean = false): Url {
        val base = hostUrlProvider.get()

        return URLBuilder(base).apply {
            this.appendPathSegments(components = path, encodeSlash = encodeSlash)
        }.build()
    }
}
