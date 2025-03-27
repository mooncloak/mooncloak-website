@file:JsModule("ethers")

package com.mooncloak.website.feature.billing.external

import kotlin.js.Promise

// JsonRpcProvider interface
internal external interface JsonRpcProvider : JsAny

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

internal external val ethers: JsAny
