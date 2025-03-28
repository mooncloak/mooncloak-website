package com.mooncloak.website.feature.billing.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mooncloak.website.feature.billing.model.CryptoCurrency

@Composable
internal fun PayWithCryptoLayout(
    uri: String?,
    address: String?,
    priceTitle: String?,
    paymentStatusTitle: String,
    paymentStatusDescription: String?,
    paymentStatusPending: Boolean,
    selectedCurrency: CryptoCurrency,
    currencies: Collection<CryptoCurrency>,
    onCopiedAddress: () -> Unit,
    onOpenWallet: () -> Unit,
    onRefreshStatus: () -> Unit,
    onCurrencySelected: (currency: CryptoCurrency) -> Unit,
    modifier: Modifier = Modifier,
    actionOpenVisible: Boolean = true,
    actionOpenEnabled: Boolean = true,
    actionRefreshVisible: Boolean = true
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CryptoInvoiceLayout(
            modifier = Modifier.sizeIn(maxWidth = 600.dp)
                .fillMaxWidth(),
            uri = uri,
            address = address,
            priceTitle = priceTitle,
            paymentStatusTitle = paymentStatusTitle,
            paymentStatusPending = paymentStatusPending,
            paymentStatusDescription = paymentStatusDescription,
            selectedCurrency = selectedCurrency,
            currencies = currencies,
            actionOpenVisible = actionOpenVisible,
            actionOpenEnabled = actionOpenEnabled,
            actionRefreshVisible = actionRefreshVisible,
            onOpenWallet = onOpenWallet,
            onCopiedAddress = onCopiedAddress,
            onRefreshStatus = onRefreshStatus,
            onCurrencySelected = onCurrencySelected
        )
    }
}
