package com.mooncloak.website.feature.download.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.mooncloak.moonscape.theme.MooncloakTheme
import com.mooncloak.website.feature.download.Res
import com.mooncloak.website.feature.download.action_try_now
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DownloadAppSection(
    onDownload: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .sizeIn(maxWidth = 400.dp)
                .fillMaxWidth()
                .pointerHoverIcon(PointerIcon.Hand),
            onClick = onDownload
        ) {
            Text(
                text = stringResource(Res.string.action_try_now),
                style = MooncloakTheme.typography.headlineSmall
            )
        }
    }
}
