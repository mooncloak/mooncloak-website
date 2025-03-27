package com.mooncloak.website.feature.billing

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mooncloak.kodetools.logpile.core.LogPile
import com.mooncloak.kodetools.logpile.core.info
import com.mooncloak.moonscape.theme.MooncloakTheme
import com.mooncloak.website.feature.billing.api.HostUrlProvider
import com.mooncloak.website.feature.billing.api.HttpBillingApi
import com.mooncloak.website.feature.billing.api.Mooncloak
import com.mooncloak.website.feature.billing.crypto.CryptoChainlinkAddressProvider
import com.mooncloak.website.feature.billing.crypto.CryptoUSDPriceFetcher
import com.mooncloak.website.feature.billing.crypto.Default
import com.mooncloak.website.feature.billing.crypto.invoke
import io.ktor.client.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json

public actual object BillingFeature : BaseBillingFeature() {

    private val clock = Clock.System

    private val json: Json = Json {
        ignoreUnknownKeys = true
    }

    private val httpClient: HttpClient = HttpClient {
        // Installs support for content negotiation with the server. This allows us to submit and
        // receive the HTTP requests and responses in a particular format (ex: JSON).
        // https://ktor.io/docs/client-serialization.html
        install(ContentNegotiation) {
            json(json)
        }

        // Installs an HTTP cache. Currently, only an in-memory cache is supported. This makes the
        // same HTTP requests respond with the cached data instead of requiring to make the call
        // again, resulting in a performance boost.
        // https://ktor.io/docs/client-caching.html
        // FIXME: Re-enable HTTP Cache: install(HttpCache)
        // The HTTP Cache is causing issues, specifically the following error on load from the cache:
        // `java.lang.IllegalStateException: No instance for key AttributeKey: CallLogger`
        // It incorrectly points to the `CallLogger` component, even though it is in fact the HttpCache.
        // Re-enable when you figure out how to fix this. Note, the call that always seems to fail is the call to load
        // all the contributors.

        // Installs support for content encoding. This handles compression and decompression of
        // HTTP requests and responses automatically for the supported algorithms.
        // https://ktor.io/docs/client-content-encoding.html
        install(ContentEncoding) {
            identity()
            deflate()
            gzip()
        }

        // Installs logging for HTTP requests and responses. These are only logged during
        // development because our logger (LogPile) only logs when in "debug" mode. Seeing these
        // logs helps us determine issues within the application (like why an API request failed).
        // https://ktor.io/docs/client-logging.html#configure_plugin
        install(Logging) {
            logger = object : Logger {

                override fun log(message: String) {
                    LogPile.info(message)
                }
            }

            level = LogLevel.ALL

            // Remove the Authorization header value from being logged even in development.
            // https://ktor.io/docs/client-logging.html#configure_plugin
            sanitizeHeader { header ->
                header == HttpHeaders.Authorization
            }
        }
    }

    private val billingApi = HttpBillingApi(
        httpClient = httpClient,
        hostUrlProvider = HostUrlProvider.Mooncloak
    )

    private val addressProvider = CryptoChainlinkAddressProvider.Default
    private val priceFetcher = CryptoUSDPriceFetcher.invoke(addressProvider = addressProvider)

    @Composable
    override fun Content() {
        MooncloakTheme {
            BillingScreen(
                modifier = Modifier.fillMaxSize(),
                billingApi = billingApi,
                clock = clock,
                priceFetcher = priceFetcher,
                addressProvider = addressProvider
            )
        }
    }
}
