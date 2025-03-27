package com.mooncloak.website.feature.billing.crypto

import com.mooncloak.website.feature.billing.model.CryptoCurrency

public interface CryptoUSDPriceFetcher {

    public suspend fun fetch(currency: CryptoCurrency): Double?

    public companion object
}

public operator fun CryptoUSDPriceFetcher.Companion.invoke(
    addressProvider: CryptoChainlinkAddressProvider = CryptoChainlinkAddressProvider.Default,
    cryptoRpc: CryptoRpc
): CryptoUSDPriceFetcher = DefaultCryptoUSDPriceFetcher(
    addressProvider = addressProvider,
    cryptoRpc = cryptoRpc
)

// Wrapper class to fetch crypto prices
private class DefaultCryptoUSDPriceFetcher(
    private val addressProvider: CryptoChainlinkAddressProvider = CryptoChainlinkAddressProvider.Default,
    private val cryptoRpc: CryptoRpc
) : CryptoUSDPriceFetcher {

    override suspend fun fetch(currency: CryptoCurrency): Double? {
        val address = addressProvider[currency] ?: return null

        return cryptoRpc.getPriceFromFeed(feedAddress = address)
    }
}
