package com.mooncloak.website.feature.billing.api

import com.mooncloak.website.feature.billing.model.PaymentLink
import com.mooncloak.website.feature.billing.model.Plan

public interface BillingApi {

    public suspend fun getProduct(id: String): Plan

    public suspend fun getPaymentLinks(): List<PaymentLink>

    public companion object
}
