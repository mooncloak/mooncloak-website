package com.mooncloak.website.feature.billing.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
public sealed interface BillingDestination {

    public val path: String

    @Immutable
    @Serializable
    public data object Landing : BillingDestination {

        override val path: String = "/billing"
    }

    @Immutable
    @Serializable
    public data object PayWithCrypto : BillingDestination {

        override val path: String = "/billing/crypto"
    }

    @Immutable
    @Serializable
    public data object Success : BillingDestination {

        override val path: String = "/billing/success"
    }

    public companion object
}
