package com.mooncloak.website.feature.billing.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.mooncloak.website.feature.billing.Res
import com.mooncloak.website.feature.billing.action_pay_with_card
import com.mooncloak.website.feature.billing.action_pay_with_crypto
import com.mooncloak.website.feature.billing.composable.PlansContainer
import com.mooncloak.website.feature.billing.composable.TermsAndConditions
import com.mooncloak.website.feature.billing.model.Plan
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun LandingLayout(
    selectedPlan: Plan?,
    plans: List<Plan>,
    noticeText: String?,
    termsAndConditionsText: AnnotatedString,
    payButtonsEnabled: Boolean,
    acceptedTerms: Boolean,
    onAcceptedTermsToggled: (accepted: Boolean) -> Unit,
    onPayWithCard: () -> Unit,
    onPayWithCrypto: () -> Unit,
    onPlanSelected: (plan: Plan) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.sizeIn(maxWidth = 600.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PlansContainer(
                modifier = Modifier.sizeIn(maxWidth = 600.dp)
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                selectedPlan = selectedPlan,
                plans = plans,
                onPlanSelected = onPlanSelected
            )

            Spacer(
                modifier = Modifier.sizeIn(minHeight = 32.dp, maxHeight = 400.dp)
                    .weight(1f)
            )

            TermsAndConditions(
                modifier = Modifier.sizeIn(maxWidth = 600.dp)
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                noticeText = noticeText,
                termsAndConditionsText = termsAndConditionsText,
                acceptedTerms = acceptedTerms,
                onAcceptedTermsToggled = onAcceptedTermsToggled
            )

            FlowRow(
                modifier = Modifier.padding(top = 32.dp)
                    .sizeIn(maxWidth = 600.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    modifier = Modifier.sizeIn(maxWidth = 400.dp)
                        .fillMaxWidth()
                        .pointerHoverIcon(PointerIcon.Hand),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    enabled = payButtonsEnabled,
                    onClick = onPayWithCard
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.CreditCard,
                        contentDescription = null
                    )

                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(Res.string.action_pay_with_card)
                    )
                }

                Button(
                    modifier = Modifier.sizeIn(maxWidth = 400.dp)
                        .fillMaxWidth()
                        .pointerHoverIcon(PointerIcon.Hand),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    enabled = payButtonsEnabled,
                    onClick = onPayWithCrypto
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.QrCode,
                        contentDescription = null
                    )

                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(Res.string.action_pay_with_crypto)
                    )
                }
            }
        }
    }
}
