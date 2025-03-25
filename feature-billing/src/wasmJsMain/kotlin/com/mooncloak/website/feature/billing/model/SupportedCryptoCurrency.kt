package com.mooncloak.website.feature.billing.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.mooncloak.website.feature.billing.*
import org.jetbrains.compose.resources.stringResource

@Immutable
public enum class SupportedCryptoCurrency(
    public val order: Int,
    public val currencyCode: String
) {

    Lunaris(order = 0, currencyCode = "LNRS"),
    Bitcoin(order = 1, currencyCode = "BTC"),
    Ethereum(order = 2, currencyCode = "ETH");

    public companion object
}

public val SupportedCryptoCurrency.title: String
    @Composable
    get() = when (this) {
        SupportedCryptoCurrency.Bitcoin -> stringResource(Res.string.crypto_name_bitcoin)
        SupportedCryptoCurrency.Ethereum -> stringResource(Res.string.crypto_name_ethereum)
        SupportedCryptoCurrency.Lunaris -> stringResource(Res.string.crypto_name_lunaris)
    }

public val SupportedCryptoCurrency.ticker: String
    @Composable
    get() = when (this) {
        SupportedCryptoCurrency.Bitcoin -> stringResource(Res.string.crypto_ticker_bitcoin)
        SupportedCryptoCurrency.Ethereum -> stringResource(Res.string.crypto_ticker_ethereum)
        SupportedCryptoCurrency.Lunaris -> stringResource(Res.string.crypto_ticker_lunaris)
    }

public val SupportedCryptoCurrency.Companion.orderedSet: Set<SupportedCryptoCurrency>
    inline get() = SupportedCryptoCurrency.entries.sortedBy { it.order }.toSet()
