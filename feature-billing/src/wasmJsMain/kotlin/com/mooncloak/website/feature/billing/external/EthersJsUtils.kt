package com.mooncloak.website.feature.billing.external

@Suppress("UNUSED_PARAMETER")
internal fun createJsonRpcProviderJsAny(url: JsString): JsAny =
    js("new ethers.JsonRpcProvider(url)")

internal fun createContractJsAny(address: JsString, abi: JsArray<JsString>, provider: JsAny): JsAny =
    js("new ethers.Contract(address, abi, provider)")

internal fun JsonRpcProvider(url: String): JsonRpcProvider =
    createJsonRpcProviderJsAny(url.toJsString()).unsafeCast()

internal fun Contract(address: String, abi: List<String>, provider: JsAny): Contract =
    createContractJsAny(
        address = address.toJsString(),
        abi = abi.map { it.toJsString() }.toJsArray(),
        provider = provider
    ).unsafeCast()
