package com.mooncloak.website.feature.download.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mooncloak.moonscape.theme.MooncloakColorPalette
import com.mooncloak.website.feature.download.Res
import com.mooncloak.website.feature.download.app_slogan_line_1
import com.mooncloak.website.feature.download.app_slogan_line_2
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun GoDarkStayBrightSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.weight(1f))

        HeadlineText(
            text = stringResource(Res.string.app_slogan_line_1),
            color = MooncloakColorPalette.MooncloakDarkPrimary
        )

        HeadlineText(
            text = stringResource(Res.string.app_slogan_line_2),
            color = MooncloakColorPalette.MooncloakYellow
        )
    }
}
