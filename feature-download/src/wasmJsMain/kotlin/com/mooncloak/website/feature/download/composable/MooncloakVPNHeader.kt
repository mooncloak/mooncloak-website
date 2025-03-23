package com.mooncloak.website.feature.download.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.moonscape.theme.MooncloakColorPalette
import com.mooncloak.website.feature.download.Res
import com.mooncloak.website.feature.download.app_name
import com.mooncloak.website.feature.download.ic_logo_mooncloak
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun MooncloakVPNHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.wrapContentSize()
                .background(
                    color = MooncloakColorPalette.MooncloakDarkPrimary,
                    shape = MaterialTheme.shapes.large
                )
                .padding(start = 16.dp, end = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(96.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(MooncloakColorPalette.MooncloakDarkPrimary),
                painter = painterResource(Res.drawable.ic_logo_mooncloak),
                contentDescription = null
            )

            Text(
                modifier = Modifier.wrapContentSize()
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically),
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    lineBreak = LineBreak.Heading
                ),
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
