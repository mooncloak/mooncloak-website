package com.mooncloak.website.feature.billing.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.mooncloak.website.feature.billing.*
import org.jetbrains.compose.resources.stringResource

@Immutable
public enum class CryptoCurrency(
    public val order: Int,
    public val currencyCode: String
) {

    Lunaris(order = 0, currencyCode = "LNRS"),
    Bitcoin(order = 1, currencyCode = "BTC"),
    Ethereum(order = 2, currencyCode = "ETH"),
    POL(order = 3, currencyCode = "POL"),
    USDC(order = 4, currencyCode = "USDC");

    public companion object
}

public val CryptoCurrency.title: String
    @Composable
    get() = when (this) {
        CryptoCurrency.Bitcoin -> stringResource(Res.string.crypto_name_bitcoin)
        CryptoCurrency.Ethereum -> stringResource(Res.string.crypto_name_ethereum)
        CryptoCurrency.Lunaris -> stringResource(Res.string.crypto_name_lunaris)
        CryptoCurrency.POL -> stringResource(Res.string.crypto_name_pol)
        CryptoCurrency.USDC -> stringResource(Res.string.crypto_name_usdc)
    }

public val CryptoCurrency.ticker: String
    @Composable
    get() = when (this) {
        CryptoCurrency.Bitcoin -> stringResource(Res.string.crypto_ticker_bitcoin)
        CryptoCurrency.Ethereum -> stringResource(Res.string.crypto_ticker_ethereum)
        CryptoCurrency.Lunaris -> stringResource(Res.string.crypto_ticker_lunaris)
        CryptoCurrency.POL -> stringResource(Res.string.crypto_ticker_pol)
        CryptoCurrency.USDC -> stringResource(Res.string.crypto_ticker_usdc)
    }

public val CryptoCurrency.Companion.orderedSet: Set<CryptoCurrency>
    inline get() = CryptoCurrency.entries.sortedBy { it.order }.toSet()
