package com.mooncloak.website.feature.billing.crypto

import com.mooncloak.website.feature.billing.external.JsonRpcProvider
import com.mooncloak.website.feature.billing.external.ethers
import com.mooncloak.website.feature.billing.model.CryptoCurrency
import kotlinx.coroutines.await
import kotlin.math.pow

public interface CryptoUSDPriceFetcher {

    public suspend fun fetch(currency: CryptoCurrency): Double?

    public companion object
}

public operator fun CryptoUSDPriceFetcher.Companion.invoke(
    addressProvider: CryptoChainlinkAddressProvider = CryptoChainlinkAddressProvider.Default
): CryptoUSDPriceFetcher = DefaultCryptoUSDPriceFetcher(addressProvider = addressProvider)

// Wrapper class to fetch crypto prices
private class DefaultCryptoUSDPriceFetcher(
    private val rpcProvider: JsonRpcProvider = ethers.providers.JsonRpcProvider("https://polygon-rpc.com"),
    private val addressProvider: CryptoChainlinkAddressProvider = CryptoChainlinkAddressProvider.Default
) : CryptoUSDPriceFetcher {

    private val priceFeedAbi = arrayOf(
        "function latestAnswer() external view returns (int256)".toJsString(),
        "function decimals() external view returns (uint8)".toJsString()
    ).toJsArray()

    private val pairAbi = arrayOf(
        "function getReserves() external view returns (uint112 reserve0, uint112 reserve1, uint32 blockTimestampLast)".toJsString()
    ).toJsArray()

    override suspend fun fetch(currency: CryptoCurrency): Double? {
        val address = addressProvider[currency] ?: return null

        return getPriceFromFeed(feedAddress = address)
    }

    // Fetch price from a Chainlink Price Feed
    private suspend fun getPriceFromFeed(feedAddress: String): Double {
        val contract = ethers.Contract(feedAddress, priceFeedAbi, rpcProvider)
        val price = contract.latestAnswer().await<JsAny>().toString().toDouble()
        val decimals = contract.decimals().await<JsAny>().toString().toInt()

        return price / 10.0.pow(decimals.toDouble())
    }
}
