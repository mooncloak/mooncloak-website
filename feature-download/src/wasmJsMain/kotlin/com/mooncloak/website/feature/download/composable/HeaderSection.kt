package com.mooncloak.website.feature.download.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
internal fun HeaderSection(
    uriHandler: UriHandler = LocalUriHandler.current,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MooncloakVPNHeader(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 32.dp)
        )

        AsyncImage(
            modifier = Modifier.width(200.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(top = 24.dp)
                .padding(horizontal = 16.dp)
                .clickable {
                    uriHandler.openUri("https://play.google.com/store/apps/details?id=com.mooncloak.vpn.app.android.play")
                }
                .pointerHoverIcon(PointerIcon.Hand),
            contentScale = ContentScale.FillWidth,
            model = "https://cdn.mooncloak.com/app/latest/assets/image/google-play/GetItOnGooglePlay_Badge_Web_color_English.png",
            contentDescription = "Get on Google Play"
        )
    }
}
