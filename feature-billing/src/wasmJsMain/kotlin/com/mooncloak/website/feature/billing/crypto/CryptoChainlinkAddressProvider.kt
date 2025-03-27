package com.mooncloak.website.feature.billing.crypto

import com.mooncloak.website.feature.billing.model.CryptoCurrency

public interface CryptoChainlinkAddressProvider {

    public operator fun get(currency: CryptoCurrency): String?

    public companion object
}

public val CryptoChainlinkAddressProvider.Companion.Default: CryptoChainlinkAddressProvider
    get() = DefaultCryptoChainlinkAddressProvider

private object DefaultCryptoChainlinkAddressProvider : CryptoChainlinkAddressProvider {

    // Chainlink Price Feed addresses on Polygon
    private val priceFeeds = mapOf(
        CryptoCurrency.POL to "0xAB594600376Ec9fD91F8e885dADF0CE036862dE0", // MATIC/USD
        CryptoCurrency.USDC to "0xfE4A8cc5b5B2366C1B58Bea3858e81843581b2F7"   // USDC/USD
        // LNRS not included as it lacks a public Chainlink feed
    )

    override fun get(currency: CryptoCurrency): String? =
        priceFeeds[currency]
}
