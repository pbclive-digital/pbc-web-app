package com.kavi.pbc.web.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import com.kavi.pbc.web.common.ui.util.ScreenType
import com.kavi.pbc.web.common.ui.util.UIUtil
import org.jetbrains.compose.resources.stringResource
import pbcwebapp.lib_common_ui.generated.resources.Res
import pbcwebapp.lib_common_ui.generated.resources.common_label_info

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

@Composable
fun AppMessageDialog(
    showDialog: MutableState<Boolean>,
    title: String = stringResource(Res.string.common_label_info),
    message: String,
    actionButtonText: String,
    onDismiss: () -> Unit
) {
    BoxWithConstraints {
        val maxWidth = this.maxWidth
        val screenType = UIUtil.screenType(maxWidth = maxWidth)

        val modifier = when(screenType) {
            ScreenType.PHONE -> {
                Modifier
                    .fillMaxWidth()
            }
            else -> {
                Modifier
                    .width(500.dp)
            }
        }

        AppBasicDialog(
            modifier = modifier,
            showDialog = showDialog.value,
            onDismissRequest = {
                onDismiss.invoke()
            }
        ) {
            Box (
                modifier = when(screenType) {
                    ScreenType.PHONE -> Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(25.dp)
                        .fillMaxWidth()
                    else -> Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(25.dp)
                        .width(500.dp)
                }
            ) {
                Column {
                    Text(
                        text = title,
                        fontFamily = PBCFontFamily,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = message,
                        fontFamily = PBCFontFamily,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Start,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row {
                        Spacer(modifier = Modifier.weight(1f))
                        AppFilledButton(
                            label = actionButtonText
                        ) {
                            onDismiss.invoke()
                        }
                    }
                }
            }
        }
    }
}