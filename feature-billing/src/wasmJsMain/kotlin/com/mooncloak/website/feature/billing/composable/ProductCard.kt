package com.mooncloak.website.feature.billing.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mooncloak.moonscape.theme.MooncloakTheme
import com.mooncloak.website.feature.billing.Res
import com.mooncloak.website.feature.billing.model.Plan
import com.mooncloak.website.feature.billing.title_loading_product_details
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ProductCard(
    plan: Plan?,
    modifier: Modifier = Modifier
) {
    val formattedPrice = remember(plan) { mutableStateOf<String?>(null) }

    LaunchedEffect(plan) {
        // TODO: Handle formatting locally if it is not provided from the API.
        formattedPrice.value = plan?.price?.formatted
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors()
    ) {
        ListItem(
            modifier = Modifier.fillMaxWidth(),
            leadingContent = {
                AnimatedContent(
                    targetState = plan
                ) { targetPlan ->
                    Box(
                        modifier = Modifier.width(64.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (targetPlan == null) {
                            CircularProgressIndicator()
                        } else {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null
                            )
                        }
                    }
                }
            },
            headlineContent = {
                Text(
                    modifier = Modifier.wrapContentSize()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    text = plan?.title ?: stringResource(Res.string.title_loading_product_details),
                    style = MaterialTheme.typography.titleMedium.copy(
                        lineBreak = LineBreak.Heading
                    ),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start
                )
            },
            supportingContent = (@Composable {
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = plan?.description?.value ?: "",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineBreak = LineBreak.Paragraph
                    ),
                    color = LocalContentColor.current.copy(alpha = MooncloakTheme.alphas.secondary)
                )
            }).takeIf { !plan?.description?.value.isNullOrBlank() },
            trailingContent = (@Composable {
                Text(
                    text = formattedPrice.value ?: "",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }).takeIf { !formattedPrice.value.isNullOrBlank() }
        )
    }
}
