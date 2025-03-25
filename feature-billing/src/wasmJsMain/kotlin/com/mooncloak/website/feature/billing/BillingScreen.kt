package com.mooncloak.website.feature.billing

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.mooncloak.moonscape.snackbar.MooncloakSnackbar
import com.mooncloak.moonscape.snackbar.showSuccess
import com.mooncloak.moonscape.theme.MooncloakColorPalette
import com.mooncloak.website.feature.billing.api.BillingApi
import com.mooncloak.website.feature.billing.composable.MooncloakTooltipBox
import com.mooncloak.website.feature.billing.layout.LandingLayout
import com.mooncloak.website.feature.billing.layout.PayWithCryptoLayout
import com.mooncloak.website.feature.billing.layout.SuccessLayout
import com.mooncloak.website.feature.billing.model.BillingDestination
import com.mooncloak.website.feature.billing.model.PlanPaymentStatus
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BillingScreen(
    billingApi: BillingApi,
    modifier: Modifier = Modifier
) {
    val viewModel = remember { BillingViewModel(billingApi = billingApi) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val uriHandler = LocalUriHandler.current
    val destination = remember { mutableStateOf(viewModel.state.current.value.startDestination) }

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(Res.string.site_name))
                },
                navigationIcon = {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimatedVisibility(
                            visible = destination.value == BillingDestination.PayWithCrypto,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            MooncloakTooltipBox(
                                text = stringResource(Res.string.action_navigate_back)
                            ) {
                                IconButton(
                                    modifier = Modifier.padding(end = 8.dp)
                                        .pointerHoverIcon(PointerIcon.Hand),
                                    onClick = {
                                        destination.value = BillingDestination.Landing
                                    }
                                ) {
                                    Icon(
                                        modifier = Modifier.padding(4.dp)
                                            .size(24.dp)
                                            .clip(CircleShape),
                                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                        contentDescription = stringResource(Res.string.action_navigate_back)
                                    )
                                }
                            }
                        }

                        MooncloakTooltipBox(
                            text = stringResource(Res.string.action_navigate_home)
                        ) {
                            Image(
                                modifier = Modifier.size(48.dp)
                                    .clip(MaterialTheme.shapes.large)
                                    .background(MooncloakColorPalette.MooncloakDarkPrimary)
                                    .clickable {
                                        uriHandler.openUri("https://mooncloak.com")
                                    }
                                    .pointerHoverIcon(PointerIcon.Hand),
                                painter = painterResource(Res.drawable.ic_logo_mooncloak),
                                contentDescription = null
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { snackbarData ->
                    MooncloakSnackbar(snackbarData = snackbarData)
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            AnimatedContent(
                targetState = destination.value,
            ) { targetDestination ->
                when (targetDestination) {
                    BillingDestination.Landing -> LandingLayout(
                        modifier = Modifier.fillMaxSize()
                            .padding(16.dp),
                        product = viewModel.state.current.value.product,
                        onPayWithCard = {
                            uriHandler.openUri(viewModel.state.current.value.billingCardPaymentUri)
                        },
                        onPayWithCrypto = {
                            destination.value = BillingDestination.PayWithCrypto
                        }
                    )

                    BillingDestination.Success -> SuccessLayout(
                        modifier = Modifier.fillMaxSize()
                            .padding(16.dp),
                        token = viewModel.state.current.value.paymentStatus?.token,
                        onCopiedToken = {
                            coroutineScope.launch {
                                snackbarHostState.showSuccess(message = getString(Res.string.success_copied_token))
                            }
                        }
                    )

                    BillingDestination.PayWithCrypto -> PayWithCryptoLayout(
                        modifier = Modifier.fillMaxSize()
                            .padding(16.dp),
                        uri = viewModel.state.current.value.invoice?.uri,
                        address = viewModel.state.current.value.invoice?.address,
                        paymentStatusTitle = viewModel.state.current.value.paymentStatus?.title
                            ?: stringResource(Res.string.label_payment_loading),
                        paymentStatusPending = viewModel.state.current.value.paymentStatus?.let { status ->
                            status is PlanPaymentStatus.Pending
                        } ?: true,
                        paymentStatusDescription = null,
                        selectedCurrency = viewModel.state.current.value.selectedCryptoCurrency,
                        currencies = viewModel.state.current.value.cryptoCurrencies,
                        actionOpenVisible = viewModel.state.current.value.invoice != null,
                        actionOpenEnabled = true,
                        actionRefreshVisible = viewModel.state.current.value.invoice != null,
                        onCopiedAddress = {
                            coroutineScope.launch {
                                snackbarHostState.showSuccess(message = getString(Res.string.success_copied_address))
                            }
                        },
                        onOpenWallet = {
                            viewModel.state.current.value.invoice?.uri?.let { walletUri ->
                                uriHandler.openUri(walletUri)
                            }
                        },
                        onRefreshStatus = viewModel::refreshStatus,
                        onCurrencySelected = viewModel::selectCurrency
                    )
                }
            }

            AnimatedVisibility(
                modifier = Modifier.align(Alignment.Center),
                visible = viewModel.state.current.value.isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CircularProgressIndicator()
            }
        }
    }

    LaunchedEffect(viewModel.state.current.value.successMessage) {
        viewModel.state.current.value.successMessage?.let { notification ->
            snackbarHostState.showSuccess(notification = notification)
        }
    }

    LaunchedEffect(viewModel.state.current.value.errorMessage) {
        viewModel.state.current.value.errorMessage?.let { notification ->
            snackbarHostState.showSuccess(notification = notification)
        }
    }
}
