package com.mooncloak.website.feature.tip.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.moonscape.theme.MooncloakTheme
import com.mooncloak.website.feature.tip.Res
import com.mooncloak.website.feature.tip.model.TipLinkItem
import com.mooncloak.website.feature.tip.tip_link_item_action_tip
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun TipLinkItemCard(
    item: TipLinkItem,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Card(
        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand)
            .then(modifier),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        onClick = onSelected
    ) {
        ListItem(
            modifier = Modifier.fillMaxWidth(),
            colors = ListItemDefaults.colors(
                containerColor = containerColor,
                headlineColor = contentColor,
                supportingColor = contentColor
            ),
            leadingContent = {
                Icon(
                    modifier = Modifier.padding(end = 16.dp)
                        .size(64.dp),
                    painter = item.icon.invoke(),
                    contentDescription = item.contentDescription.invoke(),
                    tint = contentColor
                )
            },
            headlineContent = {
                Text(
                    modifier = Modifier,
                    text = item.title.invoke(),
                    style = MaterialTheme.typography.titleMedium,
                    color = contentColor,
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingContent = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier,
                        text = item.description.invoke(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = contentColor.copy(alpha = MooncloakTheme.alphas.secondary),
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis
                    )

                    TextButton(
                        modifier = Modifier.align(Alignment.End),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = contentColor
                        ),
                        onClick = onSelected
                    ) {
                        Text(
                            text = stringResource(Res.string.tip_link_item_action_tip, item.price),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
        )
    }
}
