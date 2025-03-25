package com.mooncloak.website.feature.billing

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.mooncloak.moonscape.snackbar.MooncloakSnackbar
import com.mooncloak.moonscape.snackbar.showSuccess

@Composable
internal fun BillingScreen(
    modifier: Modifier = Modifier
) {
    val viewModel = remember { BillingViewModel() }
    val snackbarHostState = remember { SnackbarHostState() }
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { snackbarData ->
                    MooncloakSnackbar(snackbarData = snackbarData)
                }
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {


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
