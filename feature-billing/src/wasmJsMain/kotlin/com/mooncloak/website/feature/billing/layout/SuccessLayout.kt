package com.mooncloak.website.feature.billing.layout

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mooncloak.website.feature.billing.*
import com.mooncloak.website.feature.billing.composable.GenericHeaderGraphic
import com.mooncloak.website.feature.billing.composable.MooncloakTooltipBox
import com.mooncloak.website.feature.billing.model.TransactionToken
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SuccessLayout(
    token: TransactionToken?,
    redirectUri: String?,
    onCopiedToken: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current
    val clipboardManager = LocalClipboardManager.current

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.sizeIn(maxWidth = 600.dp)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GenericHeaderGraphic(
                modifier = Modifier.size(128.dp),
                painter = rememberVectorPainter(Icons.Default.Check),
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            )

            Text(
                modifier = Modifier.padding(top = 32.dp),
                text = stringResource(Res.string.title_success),
                style = MaterialTheme.typography.titleLarge
            )

            if (token != null) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 32.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(5.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SelectionContainer {
                        Text(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            text = token.value,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            textAlign = TextAlign.Start
                        )
                    }

                    Spacer(
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        modifier = Modifier.padding(start = 16.dp)
                            .pointerHoverIcon(PointerIcon.Hand),
                        onClick = {
                            clipboardManager.setText(AnnotatedString(token.value))
                            onCopiedToken.invoke()
                        }
                    ) {
                        MooncloakTooltipBox(
                            text = stringResource(Res.string.action_copy_token)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = stringResource(Res.string.action_copy_token)
                            )
                        }
                    }
                }
            }

            Button(
                modifier = Modifier.sizeIn(maxWidth = 400.dp)
                    .fillMaxWidth()
                    .padding(top = 32.dp)
                    .pointerHoverIcon(PointerIcon.Hand),
                enabled = redirectUri != null,
                onClick = {
                    redirectUri?.let {
                        uriHandler.openUri(it)
                    }
                }
            ) {
                Text(
                    text = stringResource(Res.string.action_open_app)
                )
            }
        }
    }
}
