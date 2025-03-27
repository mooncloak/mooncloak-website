package com.mooncloak.website.feature.billing.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.moonscape.theme.MooncloakTheme
import com.mooncloak.website.feature.billing.*
import com.mooncloak.website.feature.billing.composable.MooncloakTooltipBox
import com.mooncloak.website.feature.billing.model.CryptoCurrency
import com.mooncloak.website.feature.billing.model.ticker
import io.github.alexzhirkevich.qrose.options.QrBrush
import io.github.alexzhirkevich.qrose.options.solid
import io.github.alexzhirkevich.qrose.rememberQrCodePainter
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CryptoInvoiceLayout(
    uri: String?,
    address: String?,
    paymentStatusTitle: String,
    paymentStatusDescription: String?,
    paymentStatusPending: Boolean,
    selectedCurrency: CryptoCurrency,
    currencies: Collection<CryptoCurrency>,
    onOpenWallet: () -> Unit,
    onCopiedAddress: () -> Unit,
    onRefreshStatus: () -> Unit,
    onCurrencySelected: (currency: CryptoCurrency) -> Unit,
    actionOpenVisible: Boolean = true,
    actionOpenEnabled: Boolean = true,
    actionRefreshVisible: Boolean = true,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    val containerQrBrush = QrBrush.solid(MaterialTheme.colorScheme.surface)
    val contentQrBrush = QrBrush.solid(MaterialTheme.colorScheme.onSurface)
    val dropdownExpanded = remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                modifier = Modifier.sizeIn(maxWidth = 400.dp)
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .pointerHoverIcon(PointerIcon.Hand),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                onClick = {
                    dropdownExpanded.value = true
                }
            ) {
                Text(
                    text = "(${selectedCurrency.ticker}) ${selectedCurrency.name}"
                )

                Icon(
                    modifier = Modifier.padding(start = 16.dp),
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            DropdownMenu(
                modifier = Modifier.sizeIn(maxWidth = 400.dp)
                    .fillMaxWidth()
                    .align(Alignment.Center),
                expanded = dropdownExpanded.value,
                onDismissRequest = { dropdownExpanded.value = false },
                shape = RoundedCornerShape(10.dp)
            ) {
                currencies.forEach { currency ->
                    DropdownMenuItem(
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                        text = {
                            Text(
                                text = "(${currency.ticker}) ${currency.name}"
                            )
                        },
                        onClick = {
                            onCurrencySelected.invoke(currency)

                            dropdownExpanded.value = false
                        }
                    )
                }
            }
        }

        Box(
            modifier = Modifier.padding(top = 32.dp)
                .sizeIn(maxWidth = 300.dp, minHeight = 300.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (uri != null) {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = rememberQrCodePainter(
                        data = uri
                    ) {
                        // TODO: Customize the QR code style
                        colors {
                            dark = contentQrBrush
                            light = containerQrBrush
                            ball = contentQrBrush
                            frame = contentQrBrush
                        }
                    },
                    contentDescription = "Payment QR code",
                    contentScale = ContentScale.FillWidth
                )
            } else {
                CircularProgressIndicator()
            }
        }

        AnimatedVisibility(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp),
            visible = uri != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    modifier = Modifier.size(16.dp),
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = MooncloakTheme.alphas.secondary)
                )

                Text(
                    modifier = Modifier.padding(start = 8.dp)
                        .alignByBaseline(),
                    text = stringResource(Res.string.label_qr_scan),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = MooncloakTheme.alphas.secondary)
                    )
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }

        if (address != null) {
            Text(
                modifier = Modifier.wrapContentSize()
                    .align(Alignment.Start)
                    .padding(top = 32.dp),
                text = stringResource(Res.string.label_crypto_address),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = MooncloakTheme.alphas.secondary)
            )

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 16.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(5.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SelectionContainer {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = address,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        textAlign = TextAlign.Start
                    )
                }

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    modifier = Modifier.padding(start = 16.dp)
                        .pointerHoverIcon(PointerIcon.Hand),
                    onClick = {
                        clipboardManager.setText(AnnotatedString(address))
                        onCopiedAddress.invoke()
                    }
                ) {
                    MooncloakTooltipBox(
                        text = stringResource(Res.string.action_copy_address)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = stringResource(Res.string.label_copy_crypto_address)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 32.dp)
        ) {
            Text(
                modifier = Modifier.wrapContentSize()
                    .align(Alignment.Start),
                text = stringResource(Res.string.label_payment_status),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = MooncloakTheme.alphas.secondary)
            )

            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = paymentStatusTitle,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    paymentStatusDescription?.let { description ->
                        Text(
                            modifier = Modifier.padding(top = 4.dp),
                            text = description,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = MooncloakTheme.alphas.secondary)
                            )
                        )
                    }
                }

                AnimatedVisibility(
                    visible = actionRefreshVisible,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    IconButton(
                        modifier = Modifier.padding(start = 16.dp)
                            .pointerHoverIcon(PointerIcon.Hand),
                        onClick = onRefreshStatus
                    ) {
                        MooncloakTooltipBox(
                            text = stringResource(Res.string.action_refresh)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = stringResource(Res.string.action_refresh)
                            )
                        }
                    }
                }
            }

            if (paymentStatusPending) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 16.dp)
                )
            }
        }

        if (actionOpenVisible) {
            Spacer(modifier = Modifier.weight(1f))

            Button(
                modifier = Modifier.sizeIn(maxWidth = 400.dp)
                    .fillMaxWidth()
                    .padding(top = 32.dp)
                    .pointerHoverIcon(PointerIcon.Hand),
                onClick = onOpenWallet,
                enabled = actionOpenEnabled
            ) {
                Text(text = stringResource(Res.string.action_open_wallet_app))
            }
        }
    }
}
