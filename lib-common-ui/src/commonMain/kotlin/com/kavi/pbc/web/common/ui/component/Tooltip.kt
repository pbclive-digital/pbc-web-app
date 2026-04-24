package com.kavi.pbc.web.common.ui.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTooltipWrap(
    modifier: Modifier = Modifier,
    tipLabel: String,
    content: @Composable () -> Unit
) {
    TooltipBox(
        modifier = modifier,
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(),
        tooltip = {
            RichTooltip { Text( text = tipLabel ) }
        },
        state = rememberTooltipState(),
        content = content
    )
}