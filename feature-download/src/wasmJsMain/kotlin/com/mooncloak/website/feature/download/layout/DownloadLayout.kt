package com.mooncloak.website.feature.download.layout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mikepenz.hypnoticcanvas.shaderBackground
import com.mikepenz.hypnoticcanvas.shaders.MeshGradient
import com.mooncloak.moonscape.theme.MooncloakColorPalette
import com.mooncloak.website.feature.download.*
import com.mooncloak.website.feature.download.composable.*
import com.mooncloak.website.feature.download.composable.HeaderSection
import com.mooncloak.website.feature.download.model.HeadlineTextGroup
import com.mooncloak.website.feature.download.model.HeadlineTextItem
import org.jetbrains.compose.resources.stringResource
import kotlin.math.absoluteValue

@Composable
public fun DownloadLayout(
    modifier: Modifier = Modifier
) {
    val headlineTextGroups = listOf(
        HeadlineTextGroup(
            items = listOf(
                HeadlineTextItem(
                    text = stringResource(Res.string.app_slogan_line_1),
                    color = MooncloakColorPalette.MooncloakDarkPrimary
                ),
                HeadlineTextItem(
                    text = stringResource(Res.string.app_slogan_line_2),
                    color = MooncloakColorPalette.MooncloakYellow
                )
            )
        ),
        HeadlineTextGroup(
            items = listOf(
                HeadlineTextItem(text = stringResource(Res.string.headline_privacy_focused_vpn)),
                HeadlineTextItem(text = stringResource(Res.string.headline_no_tracking))
            )
        ),
        HeadlineTextGroup(
            items = listOf(
                HeadlineTextItem(text = stringResource(Res.string.headline_no_logs)),
                HeadlineTextItem(text = stringResource(Res.string.headline_no_accounts))
            )
        ),
        HeadlineTextGroup(
            items = listOf(
                HeadlineTextItem(text = stringResource(Res.string.headline_hide_ip_address)),
                HeadlineTextItem(text = stringResource(Res.string.headline_browse_privately))
            )
        )
    )
    val pagerState = rememberPagerState(pageCount = { headlineTextGroups.size })

    BoxWithConstraints(modifier = modifier) {
        Box(
            modifier = Modifier.fillMaxSize()
                .shaderBackground(
                    MeshGradient(
                        colors = arrayOf(
                            MooncloakColorPalette.MooncloakDarkPrimary,
                            MooncloakColorPalette.Purple_600,
                            MooncloakColorPalette.MooncloakYellow
                        ),
                        scale = 1.5f
                    )
                )
        )

        val maxHeight = this.maxHeight

        VerticalPager(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 32.dp),
            state = pagerState
        ) { page ->
            val pageOffset = pagerState.currentPageOffsetFraction.absoluteValue

            HeadlineTextSection(
                modifier = Modifier.fillMaxWidth()
                    .height(maxHeight)
                    .padding(vertical = 32.dp),
                group = headlineTextGroups[page],
                alpha = 1f - pageOffset.coerceIn(0f, 1f)
            )
        }

        HeaderSection(
            modifier = Modifier.padding(horizontal = 16.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )
    }
}
