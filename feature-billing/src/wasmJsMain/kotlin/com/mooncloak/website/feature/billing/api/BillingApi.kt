package com.mooncloak.website.feature.billing.api

import com.mooncloak.website.feature.billing.model.*

public interface BillingApi {

    public suspend fun getSupportedCryptoCurrencies(): List<Currency>

    public suspend fun getPlan(id: String): Plan

    public suspend fun getPlans(): List<Plan>

    public suspend fun getInvoice(
        productId: String,
        accessToken: String? = null,
        currencyCode: Currency.Code
    ): CryptoInvoice

    public suspend fun getPaymentStatus(
        token: TransactionToken
    ): BillingPaymentStatusDetails

    public companion object
}
