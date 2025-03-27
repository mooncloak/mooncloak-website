package com.mooncloak.website.feature.billing.crypto

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlin.math.pow

public class CryptoRpc public constructor(
    private val httpClient: HttpClient,
    private val rpcUrl: String,
    private val json: Json
) {

    public suspend fun getPriceFromFeed(feedAddress: String): Double {
        // 0x50d25bcd = first 4 bytes of keccak256("latestAnswer()")
        val priceHex = callContract(feedAddress, "0x50d25bcd")
        val price = hexToLong(priceHex).toDouble()

        // 0x313ce567 = first 4 bytes of keccak256("decimals()")
        val decimalsHex = callContract(feedAddress, "0x313ce567")
        val decimals = hexToInt(decimalsHex)

        return price / 10.0.pow(decimals.toDouble())
    }

    private suspend fun callContract(address: String, functionSelector: String): String {
        val request = RpcRequest(
            method = "eth_call",
            params = buildJsonArray {
                val params = CallParams(
                    to = address,
                    data = functionSelector // e.g., "0x50d25bcd" for latestAnswer()
                )

                add(
                    json.encodeToJsonElement(
                        serializer = CallParams.serializer(),
                        value = params
                    )
                )

                add(JsonPrimitive("latest"))
            }
        )

        val response = httpClient.post(rpcUrl) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<RpcResponse>()

        return response.result?.removePrefix("0x") ?: throw Exception("RPC error: ${response.error?.message}")
    }

    private fun hexToLong(hex: String): Long {
        val cleanHex = hex.trimStart('0').ifEmpty { "0" }

        return cleanHex.toLong(16)
    }

    private fun hexToInt(hex: String): Int = hexToLong(hex).toInt()
}

@Serializable
public data class RpcRequest public constructor(
    @SerialName(value = "jsonrpc") public val version: String = "2.0",
    @SerialName(value = "method") public val method: String,
    @SerialName(value = "params") public val params: JsonArray,
    @SerialName(value = "id") public val id: Int? = null
)

@Serializable
public data class CallParams public constructor(
    @SerialName(value = "to") public val to: String,
    @SerialName(value = "data") public val data: String
)

@Serializable
public data class RpcResponse public constructor(
    @SerialName(value = "jsonrpc") public val version: String? = null,
    @SerialName(value = "id") public val id: Int? = null,
    @SerialName(value = "result") public val result: String? = null,
    @SerialName(value = "error") public val error: RpcError? = null
)

@Serializable
public data class RpcError public constructor(
    @SerialName(value = "code") public val code: Int? = null,
    @SerialName(value = "message") public val message: String? = null
)
