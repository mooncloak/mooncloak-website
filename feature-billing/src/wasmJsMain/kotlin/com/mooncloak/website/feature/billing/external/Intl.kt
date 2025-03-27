package com.mooncloak.website.feature.billing.external

internal external object Intl {

    class NumberFormat(locale: String, options: NumberFormatOptions) : JsAny {

        fun format(number: Double): String
    }
}

internal external interface NumberFormatOptions : JsAny {

    val style: String
    val currency: String
    val minimumFractionDigits: Int
    val maximumFractionDigits: Int
}

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
internal fun NumberFormatOptions(
    style: String,
    currency: String,
    minimumFractionDigits: Int,
    maximumFractionDigits: Int
): NumberFormatOptions = createNumberFormatOptions(
    style = style,
    currency = currency,
    minimumFractionDigits = minimumFractionDigits,
    maximumFractionDigits = maximumFractionDigits
) as NumberFormatOptions

internal fun createNumberFormatOptions(
    style: String,
    currency: String,
    minimumFractionDigits: Int,
    maximumFractionDigits: Int
): JsAny =
    js("({ style: style, currency: currency, minimumFractionDigits: minimumFractionDigits, maximumFractionDigits: maximumFractionDigits })")
