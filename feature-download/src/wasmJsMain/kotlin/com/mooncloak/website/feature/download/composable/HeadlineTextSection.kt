package com.mooncloak.website.feature.download.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mooncloak.website.feature.download.model.HeadlineTextGroup

@Composable
internal fun HeadlineTextSection(
    group: HeadlineTextGroup,
    modifier: Modifier = Modifier,
    alpha: Float = 1f
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(modifier = Modifier.weight(1f))

        group.items.forEach { item ->
            HeadlineText(
                text = item.text,
                color = item.color.copy(alpha = alpha)
            )
        }
    }
}
