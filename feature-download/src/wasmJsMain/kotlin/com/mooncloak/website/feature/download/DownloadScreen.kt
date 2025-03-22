package com.mooncloak.website.feature.download

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mikepenz.hypnoticcanvas.shaderBackground
import com.mikepenz.hypnoticcanvas.shaders.MeshGradient
import com.mooncloak.moonscape.theme.MooncloakColorPalette
import com.mooncloak.moonscape.theme.MooncloakTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
public fun DownloadScreen(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current

    MooncloakTheme {
        Box(modifier = modifier) {
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

            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 32.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Image(
                    modifier = Modifier.padding(top = 32.dp)
                        .size(96.dp)
                        .clip(MaterialTheme.shapes.large)
                        .background(MooncloakColorPalette.MooncloakDarkPrimary),
                    painter = painterResource(Res.drawable.ic_logo_mooncloak),
                    contentDescription = null
                )

                FlowRow(
                    modifier = Modifier.sizeIn(maxWidth = 600.dp)
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
                ) {
                    Text(
                        modifier = Modifier.wrapContentSize()
                            .padding(horizontal = 32.dp)
                            .align(Alignment.CenterVertically),
                        text = stringResource(Res.string.app_name),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            lineBreak = LineBreak.Heading
                        ),
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                }

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

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 32.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = stringResource(Res.string.app_slogan_line_1),
                        maxLines = 2,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = MooncloakColorPalette.MooncloakDarkPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 64.sp,
                            lineHeight = 64.sp
                        )
                    )

                    Text(
                        text = stringResource(Res.string.app_slogan_line_2),
                        maxLines = 2,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 64.sp,
                            lineHeight = 64.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
