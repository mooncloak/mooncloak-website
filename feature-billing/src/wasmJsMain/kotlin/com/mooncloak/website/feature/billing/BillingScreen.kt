package com.mooncloak.website.feature.billing

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.mooncloak.moonscape.snackbar.MooncloakSnackbar
import com.mooncloak.moonscape.snackbar.showError
import com.mooncloak.moonscape.snackbar.showSuccess
import com.mooncloak.website.feature.billing.api.BillingApi
import com.mooncloak.website.feature.billing.layout.FailedLayout
import com.mooncloak.website.feature.billing.layout.LandingLayout
import com.mooncloak.website.feature.billing.layout.PayWithCryptoLayout
import com.mooncloak.website.feature.billing.layout.SuccessLayout
import com.mooncloak.website.feature.billing.model.BillingDestination
import com.mooncloak.website.feature.billing.model.BillingPaymentStatus
import com.mooncloak.website.feature.shared.composable.MooncloakTopAppBar
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun BillingScreen(
    billingApi: BillingApi,
    clock: Clock,
    modifier: Modifier = Modifier
) {
    val viewModel = remember {
        BillingViewModel(
            billingApi = billingApi,
            clock = clock
        )
    }
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
            MooncloakTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                logo = painterResource(Res.drawable.ic_logo_mooncloak),
                displayBack = destination.value == BillingDestination.PayWithCrypto,
                onBack = {
                    destination.value = BillingDestination.Landing
                },
                siteName = stringResource(Res.string.site_name),
                backContentDescription = stringResource(Res.string.action_navigate_back),
                homeContentDescription = stringResource(Res.string.action_navigate_home)
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
                        selectedPlan = viewModel.state.current.value.selectedPlan,
                        plans = viewModel.state.current.value.plans,
                        noticeText = viewModel.state.current.value.noticeText,
                        termsAndConditionsText = viewModel.state.current.value.termsAndConditionsText.invoke(),
                        acceptedTerms = viewModel.state.current.value.acceptedTerms,
                        payButtonsEnabled = viewModel.state.current.value.payButtonsEnabled,
                        onPayWithCard = {
                            uriHandler.openUri(viewModel.state.current.value.billingCardPaymentUri)
                        },
                        onPayWithCrypto = {
                            destination.value = BillingDestination.PayWithCrypto
                        },
                        onPlanSelected = viewModel::selectPlan,
                        onAcceptedTermsToggled = viewModel::toggleAcceptTerms
                    )

                    BillingDestination.Success -> SuccessLayout(
                        modifier = Modifier.fillMaxSize()
                            .padding(16.dp),
                        token = viewModel.state.current.value.paymentStatusDetails?.token,
                        redirectUri = viewModel.state.current.value.redirectUri,
                        onCopiedToken = {
                            coroutineScope.launch {
                                snackbarHostState.showSuccess(message = getString(Res.string.success_copied_token))
                            }
                        }
                    )

                    BillingDestination.Failed -> FailedLayout(
                        modifier = Modifier.fillMaxWidth()
                            .padding(16.dp),
                        onRetry = {
                            destination.value = BillingDestination.Landing
                        }
                    )

                    BillingDestination.PayWithCrypto -> {
                        LaunchedEffect(targetDestination) {
                            viewModel.loadInvoice()
                        }

                        PayWithCryptoLayout(
                            modifier = Modifier.fillMaxSize()
                                .padding(16.dp),
                            uri = viewModel.state.current.value.invoice?.paymentUri,
                            address = viewModel.state.current.value.invoice?.address,
                            priceTitle = viewModel.state.current.value.priceText,
                            paymentStatusTitle = viewModel.state.current.value.paymentStatusDetails?.title
                                ?: stringResource(Res.string.label_payment_loading),
                            paymentStatusPending = viewModel.state.current.value.paymentStatusDetails?.status?.let { status ->
                                status == BillingPaymentStatus.Pending
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
                                viewModel.state.current.value.invoice?.paymentUri?.let { walletUri ->
                                    uriHandler.openUri(walletUri)
                                }
                            },
                            onRefreshStatus = viewModel::refreshStatus,
                            onCurrencySelected = viewModel::selectCurrency
                        )
                    }
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
            snackbarHostState.showError(notification = notification)
        }
    }
}
