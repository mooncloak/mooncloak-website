package com.mooncloak.website.feature.billing.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.mooncloak.website.feature.billing.*
import org.jetbrains.compose.resources.stringResource

@Immutable
public enum class CryptoCurrency(
    public val order: Int,
    public val currencyCode: Currency.Code
) {

    // Bitcoin(order = 1, currencyCode = "BTC"),
    // Ethereum(order = 2, currencyCode = "ETH"),
    // Monero(order = 3, currencyCode = "XMR"),
    POL(order = 0, currencyCode = Currency.Code(value = "POL")),
    Lunaris(order = 1, currencyCode = Currency.Code(value = "LNRS")),
    USDC(order = 2, currencyCode = Currency.Code(value = "USDC")),
    Tether(order = 3, currencyCode = Currency.Code(value = "USDT"));

    public companion object {

        public operator fun get(code: Currency.Code): CryptoCurrency? =
            CryptoCurrency.entries.firstOrNull { it.currencyCode == code }
    }
}

public val CryptoCurrency.title: String
    @Composable
    get() = when (this) {
        // CryptoCurrency.Bitcoin -> stringResource(Res.string.crypto_name_bitcoin)
        // CryptoCurrency.Ethereum -> stringResource(Res.string.crypto_name_ethereum)
        // CryptoCurrency.Monero -> stringResource(Res.string.crypto_name_monero)
        CryptoCurrency.Lunaris -> stringResource(Res.string.crypto_name_lunaris)
        CryptoCurrency.POL -> stringResource(Res.string.crypto_name_pol)
        CryptoCurrency.USDC -> stringResource(Res.string.crypto_name_usdc)
        CryptoCurrency.Tether -> stringResource(Res.string.crypto_name_usdt)
    }

public val CryptoCurrency.ticker: String
    @Composable
    get() = when (this) {
        // CryptoCurrency.Bitcoin -> stringResource(Res.string.crypto_ticker_bitcoin)
        // CryptoCurrency.Ethereum -> stringResource(Res.string.crypto_ticker_ethereum)
        // CryptoCurrency.Monero -> stringResource(Res.string.crypto_ticker_monero)
        CryptoCurrency.Lunaris -> stringResource(Res.string.crypto_ticker_lunaris)
        CryptoCurrency.POL -> stringResource(Res.string.crypto_ticker_pol)
        CryptoCurrency.USDC -> stringResource(Res.string.crypto_ticker_usdc)
        CryptoCurrency.Tether -> stringResource(Res.string.crypto_ticker_usdt)
    }

public val CryptoCurrency.Companion.orderedSet: Set<CryptoCurrency>
    inline get() = CryptoCurrency.entries.sortedBy { it.order }.toSet()
