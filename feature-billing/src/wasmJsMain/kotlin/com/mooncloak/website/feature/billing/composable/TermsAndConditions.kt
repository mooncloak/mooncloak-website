package com.mooncloak.website.feature.billing.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.mooncloak.moonscape.theme.MooncloakTheme

@Composable
internal fun TermsAndConditions(
    noticeText: String?,
    termsAndConditionsText: AnnotatedString,
    acceptedTerms: Boolean,
    onAcceptedTermsToggled: (accepted: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (noticeText != null) {
            Text(
                text = noticeText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = MooncloakTheme.alphas.secondary
                )
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = if (noticeText != null) 32.dp else 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = acceptedTerms,
                onClick = {
                    onAcceptedTermsToggled.invoke(!acceptedTerms)
                },
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = MaterialTheme.colorScheme.onSurface
                )
            )

            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = termsAndConditionsText
            )
        }
    }
}
