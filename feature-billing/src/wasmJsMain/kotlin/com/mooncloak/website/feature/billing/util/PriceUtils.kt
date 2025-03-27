package com.mooncloak.website.feature.billing.util

import com.mooncloak.website.feature.billing.external.Intl
import com.mooncloak.website.feature.billing.external.NumberFormatOptions
import com.mooncloak.website.feature.billing.model.Currency
import com.mooncloak.website.feature.billing.model.Price

public fun Price.format(): String {
    // Return pre-formatted string if available
    this.formatted?.takeIf { it.isNotBlank() }?.let { return it }

    val isUsd = currency.code == Currency.Code.USD

    // Configure Intl.NumberFormat for currency
    val formatter = Intl.NumberFormat(
        locale = "en-US",
        options = NumberFormatOptions(
            style = "currency",
            currency = currency.code.value, // e.g., "USD"
            minimumFractionDigits = if (isUsd && amount % 100L == 0L) 0 else 2,
            maximumFractionDigits = if (isUsd && amount % 100L == 0L) 0 else 2
        )
    )

    // Adjust amount based on currency (cents for USD)
    val formattedAmount = when {
        isUsd -> this.amount * 0.01 // Convert cents to dollars (e.g., 800L -> 8.0)
        // TODO: Add logic for other currencies if needed (e.g., defaultFractionDigits)
        else -> this.amount.toDouble()
    }

    return formatter.format(formattedAmount)
}
