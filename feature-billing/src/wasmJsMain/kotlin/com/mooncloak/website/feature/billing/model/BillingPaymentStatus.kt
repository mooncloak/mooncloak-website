package com.mooncloak.website.feature.billing.model

import kotlinx.serialization.Serializable

@Serializable
public value class BillingPaymentStatus public constructor(
    public val value: String
) {

    public companion object {

        public val Pending: BillingPaymentStatus = BillingPaymentStatus(value = "pending")
        public val Completed: BillingPaymentStatus = BillingPaymentStatus(value = "completed")
        public val Failed: BillingPaymentStatus = BillingPaymentStatus(value = "failed")
        public val Refunded: BillingPaymentStatus = BillingPaymentStatus(value = "refunded")
    }
}
