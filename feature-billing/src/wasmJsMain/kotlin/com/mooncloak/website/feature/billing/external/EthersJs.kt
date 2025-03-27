package com.mooncloak.website.feature.billing.external

import kotlin.js.Promise

// External interface for the ethers module
internal external interface Ethers : JsAny {

    val providers: Providers
    val utils: Utils

    fun Contract(address: String, abi: JsArray<JsString>, provider: JsAny): Contract
}

// Providers interface
internal external interface Providers : JsAny {
    fun JsonRpcProvider(url: String): JsonRpcProvider
}

// JsonRpcProvider interface
internal external interface JsonRpcProvider : JsAny

// Utils interface
internal external interface Utils : JsAny {
    fun parseEther(amount: String): JsAny
}

// Reserves interface for getReserves() return value
internal external interface Reserves : JsAny {
    val reserve0: JsAny
    val reserve1: JsAny
    val blockTimestampLast: JsAny
}

// Contract interface
internal external interface Contract : JsAny {
    fun latestAnswer(): Promise<JsAny>
    fun decimals(): Promise<JsAny>
    fun getReserves(): Promise<Reserves>
}

// Declare ethers as a global variable (or imported via module)
@JsName("ethers")
internal external val ethers: Ethers
