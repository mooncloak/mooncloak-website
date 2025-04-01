package com.mooncloak.website.feature.billing.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.mooncloak.website.feature.billing.*
import com.mooncloak.website.feature.billing.composable.GenericHeaderGraphic
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun FailedLayout(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.sizeIn(maxWidth = 600.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GenericHeaderGraphic(
                modifier = Modifier.size(128.dp),
                painter = rememberVectorPainter(Icons.Default.PriorityHigh),
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )

            Text(
                modifier = Modifier.padding(top = 32.dp),
                text = stringResource(Res.string.title_failed),
                style = MaterialTheme.typography.titleLarge
            )

            Button(
                modifier = Modifier.sizeIn(maxWidth = 400.dp)
                    .fillMaxWidth()
                    .padding(top = 32.dp)
                    .pointerHoverIcon(PointerIcon.Hand),
                onClick = onRetry
            ) {
                Text(
                    text = stringResource(Res.string.action_try_again)
                )
            }
        }
    }
}
