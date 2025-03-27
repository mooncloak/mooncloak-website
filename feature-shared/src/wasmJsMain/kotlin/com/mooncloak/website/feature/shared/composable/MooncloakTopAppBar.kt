package com.mooncloak.website.feature.shared.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.unit.dp
import com.mooncloak.moonscape.theme.MooncloakColorPalette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun MooncloakTopAppBar(
    logo: Painter,
    displayBack: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    siteName: String = "mooncloak",
    backContentDescription: String = "Navigate Back",
    homeContentDescription: String = "Navigate Home",
    uriHandler: UriHandler = LocalUriHandler.current,
    homeUri: String = "https://mooncloak.com"
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(text = siteName)
        },
        navigationIcon = {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedVisibility(
                    visible = displayBack,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    MooncloakTooltipBox(
                        text = backContentDescription
                    ) {
                        IconButton(
                            modifier = Modifier.padding(end = 8.dp)
                                .pointerHoverIcon(PointerIcon.Hand),
                            onClick = onBack
                        ) {
                            Icon(
                                modifier = Modifier.padding(4.dp)
                                    .size(24.dp)
                                    .clip(CircleShape),
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = backContentDescription
                            )
                        }
                    }
                }

                MooncloakTooltipBox(
                    text = homeContentDescription
                ) {
                    Image(
                        modifier = Modifier.size(48.dp)
                            .clip(MaterialTheme.shapes.large)
                            .background(MooncloakColorPalette.MooncloakDarkPrimary)
                            .clickable {
                                uriHandler.openUri(homeUri)
                            }
                            .pointerHoverIcon(PointerIcon.Hand),
                        painter = logo,
                        contentDescription = null
                    )
                }
            }
        }
    )
}
