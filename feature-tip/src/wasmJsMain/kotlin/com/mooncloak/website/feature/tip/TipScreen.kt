package com.mooncloak.website.feature.tip

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.mooncloak.moonscape.snackbar.MooncloakSnackbar
import com.mooncloak.moonscape.theme.DefaultHorizontalPageSpacing
import com.mooncloak.moonscape.theme.MooncloakColorPalette
import com.mooncloak.website.feature.tip.composable.Header
import com.mooncloak.website.feature.tip.composable.TipLinkItemCard
import org.jetbrains.compose.resources.stringResource

@Composable
public fun TipScreen(
    modifier: Modifier = Modifier
) {
    val viewModel = remember { TipViewModel() }
    val snackbarHostState = remember { SnackbarHostState() }
    val lazyListState = rememberLazyListState()
    val uriHandler = LocalUriHandler.current

    val accentColors = remember {
        listOf(
            MooncloakColorPalette.Blue_500 to Color.White,
            MooncloakColorPalette.Teal_500 to Color.White,
            MooncloakColorPalette.Purple_600 to Color.White,
            MooncloakColorPalette.Pink_500 to Color.White
        )
    }

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
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Header(
                    modifier = Modifier.padding(top = 32.dp),
                    title = stringResource(Res.string.collaborator_list_tip_title),
                    description = stringResource(Res.string.collaborator_list_tip_description)
                )

                Spacer(modifier = Modifier.height(32.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = DefaultHorizontalPageSpacing),
                    state = lazyListState,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(
                        items = viewModel.state.current.value.items,
                        key = { _, link -> link.url },
                        contentType = { _, _ -> "TipLinkItem" }
                    ) { index, link ->
                        TipLinkItemCard(
                            modifier = Modifier.sizeIn(
                                maxWidth = 600.dp
                            ).fillMaxWidth(),
                            item = link,
                            onSelected = {
                                uriHandler.openUri(link.url)
                            },
                            containerColor = accentColors[index % accentColors.size].first,
                            contentColor = accentColors[index % accentColors.size].second
                        )
                    }

                    item(
                        key = "BottomSpacing"
                    ) {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}
