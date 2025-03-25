package com.mooncloak.website.feature.billing.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

/**
 * Represents a specific [Token] value representing a successful transaction.
 *
 * @property [value] The transaction token [String] value.
 */
@Immutable
@Serializable
public value class TransactionToken public constructor(
    public val value: String
)
