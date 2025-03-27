package com.mooncloak.website.feature.billing.api

import com.mooncloak.website.feature.billing.model.*

public interface BillingApi {

    public suspend fun getPlan(id: String): Plan

    public suspend fun getPlans(): List<Plan>

    public suspend fun getInvoice(
        productId: String,
        token: String?,
        currencyCode: String
    ): CryptoInvoice

    public suspend fun getPaymentStatus(
        token: TransactionToken
    ): PlanPaymentStatus

    public companion object
}
