package com.mooncloak.website.feature.billing.composable

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import com.mooncloak.moonscape.theme.MooncloakColorPalette
import com.mooncloak.moonscape.theme.MooncloakTheme
import com.mooncloak.website.feature.billing.Res
import com.mooncloak.website.feature.billing.label_selected_plan
import com.mooncloak.website.feature.billing.model.Plan
import com.mooncloak.website.feature.billing.util.format
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun PlansContainer(
    selectedPlan: Plan?,
    plans: List<Plan>,
    onPlanSelected: (plan: Plan) -> Unit,
    modifier: Modifier = Modifier
) {
    val dropdownExpanded = remember { mutableStateOf(false) }
    val accentColors = remember {
        listOf(
            MooncloakColorPalette.Blue_500 to Color.White,
            MooncloakColorPalette.Teal_500 to Color.White,
            MooncloakColorPalette.Purple_600 to Color.White
        )
    }
    val isInDarkMode = isSystemInDarkTheme()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.align(Alignment.Start),
            text = stringResource(Res.string.label_selected_plan),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = MooncloakTheme.alphas.secondary)
        )

        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            SelectedPlanCard(
                modifier = Modifier.fillMaxWidth()
                    .pointerHoverIcon(PointerIcon.Hand),
                plan = selectedPlan,
                onClick = {
                    dropdownExpanded.value = true
                }
            )

            DropdownMenu(
                modifier = Modifier.sizeIn(maxWidth = 600.dp)
                    .fillMaxWidth()
                    .align(Alignment.Center),
                expanded = dropdownExpanded.value,
                onDismissRequest = { dropdownExpanded.value = false },
                shape = RoundedCornerShape(10.dp)
            ) {
                AllPlansCard(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .padding(bottom = 12.dp)
                )

                plans.forEachIndexed { index, plan ->
                    DropdownMenuItem(
                        modifier = Modifier.pointerHoverIcon(PointerIcon.Hand),
                        text = {
                            PlanCard(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                title = plan.title,
                                description = plan.description?.value,
                                price = plan.price.format(),
                                highlight = plan.highlight,
                                selected = selectedPlan == plan,
                                enabled = plan.active,
                                isInDarkMode = isInDarkMode,
                                accentColor = accentColors[index % accentColors.size].first,
                                onAccentColor = accentColors[index % accentColors.size].second,
                                onSelected = {
                                    onPlanSelected.invoke(plan)

                                    dropdownExpanded.value = false
                                }
                            )
                        },
                        onClick = {
                            onPlanSelected.invoke(plan)

                            dropdownExpanded.value = false
                        }
                    )
                }
            }
        }
    }
}
