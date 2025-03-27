package com.mooncloak.website.feature.billing.external

public external interface KeyIterator : JsAny {

    public fun next(): KeyResult
}

public external interface KeyResult : JsAny {

    @JsName("done")
    public val done: Boolean

    @JsName("value")
    public val value: JsString?
}

/**
 * Exposes the JavaScript [URLSearchParams](https://developer.mozilla.org/en/docs/Web/API/URLSearchParams) to Kotlin
 */
public open external class URLSearchParams(init: JsAny? /* String|URLSearchParams */ = definedExternally) : JsAny {

    public fun append(name: String, value: String)
    public fun delete(name: String)
    public fun get(name: String): String?
    public fun getAll(name: String): JsArray<JsString>
    public fun has(name: String): Boolean
    public fun set(name: String, value: String)
    public fun keys(): KeyIterator
}

public data class QueryParameter public constructor(
    public val key: String,
    public val values: List<String> = emptyList()
)

public data class QueryParameters public constructor(
    public val parameters: List<QueryParameter> = emptyList()
) {

    private val parameterMap = parameters.associateBy { it.key }

    @Suppress("MemberVisibilityCanBePrivate")
    public fun parameters(key: String): List<String> = parameterMap[key]?.values ?: emptyList()

    public fun parameter(key: String): String? = parameters(key = key).firstOrNull()
}

@Suppress("NOTHING_TO_INLINE")
public inline operator fun QueryParameters.get(key: String): String? = this.parameter(key = key)

public fun URLSearchParams.parameters(): QueryParameters {
    val queryParameterMap = mutableMapOf<String, List<String>>()

    val iterator = keys()
    var keyItem = iterator.next()

    while (!keyItem.done) {
        val key = keyItem.value?.toString()

        if (key != null) {
            val values = this.getAll(key).toList().map { it.toString() }

            queryParameterMap[key] = values
        }

        keyItem = iterator.next()
    }

    val parameters = queryParameterMap.map { entry -> QueryParameter(key = entry.key, values = entry.value) }

    return QueryParameters(parameters = parameters)
}
