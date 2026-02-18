package com.kavi.pbc.web.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.util.ScreenType
import com.kavi.pbc.web.common.ui.util.UIUtil

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
fun AppBasicDialog(
    modifier: Modifier = Modifier,
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

    if (showDialog) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
            )
        ) {
            BoxWithConstraints (
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                val screenWidth = this.maxWidth
                val dialogWidth = screenWidth.value * 0.85
                val screenType = UIUtil.screenType(maxWidth)

                val sidePadding = when (screenType) {
                    ScreenType.PHONE -> 8.dp
                    ScreenType.TABLET, ScreenType.COMPUTER -> {
                        (maxWidth.value * .1).dp
                    }
                }

                Box(
                    Modifier
                        .padding(start = sidePadding, end = sidePadding)
                        .pointerInput(Unit) { detectTapGestures { } }
                        .shadow(8.dp, shape = RoundedCornerShape(16.dp), spotColor = themeAdditionalColors.shadow)
                        .width(dialogWidth.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            MaterialTheme.colorScheme.surface,
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    content()
                }
            }
        }
    }
}