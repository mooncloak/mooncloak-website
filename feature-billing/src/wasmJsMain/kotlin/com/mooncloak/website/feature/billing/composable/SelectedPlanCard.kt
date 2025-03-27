package com.mooncloak.website.feature.billing.composable

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.mooncloak.website.feature.billing.title_select_plan
import com.mooncloak.website.feature.billing.util.format
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SelectedPlanCard(
    plan: Plan?,
    loading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        onClick = onClick
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
                        if (loading) {
                            CircularProgressIndicator()
                        } else {
                            Text(
                                text = plan?.price?.format() ?: "",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            },
            headlineContent = {
                Text(
                    modifier = Modifier.wrapContentSize(),
                    text = plan?.title ?: if (loading) {
                        stringResource(Res.string.title_loading_product_details)
                    } else {
                        stringResource(Res.string.title_select_plan)
                    },
                    style = MaterialTheme.typography.titleMedium.copy(
                        lineBreak = LineBreak.Heading
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start
                )
            },
            supportingContent = (@Composable {
                Text(
                    modifier = Modifier.padding(top = 8.dp),
                    text = plan?.description?.value ?: "",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineBreak = LineBreak.Paragraph
                    ),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = MooncloakTheme.alphas.secondary)
                )
            }).takeIf { !plan?.description?.value.isNullOrBlank() },
            trailingContent = (@Composable {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            })
        )
    }
}
