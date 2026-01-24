package com.kavi.pbc.web.common.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily

@Composable
fun Title(modifier: Modifier = Modifier,
          titleText: String, textSize: Int = 48,
          textColor: Color = MaterialTheme.colorScheme.onBackground) {
    Row (
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = titleText,
            fontFamily = PBCFontFamily,
            fontWeight = FontWeight.Bold,
            lineHeight = textSize.sp,
            fontSize = textSize.sp,
            color = textColor
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun TitleWithAction(modifier: Modifier = Modifier,
                    titleText: String,
                    textSize: Int = 48,
                    textColor: Color = MaterialTheme.colorScheme.onBackground,
                    actionPainter: Painter,
                    actionPainterSize: Dp = 56.dp,
                    isIcon: Boolean = false,
                    action: (() -> Unit)? = null) {

    val themeAdditionalColors = LocalThemeAdditionalColors.current

    Row (
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = titleText,
            fontFamily = PBCFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = textSize.sp,
            lineHeight = textSize.sp,
            color = textColor
        )
        Spacer(modifier = Modifier.weight(1f))
        if (isIcon) {
            Icon(
                painter = actionPainter,
                contentDescription = "Provided icon",
                tint = themeAdditionalColors.shadow,
                modifier = Modifier
                    .size(actionPainterSize)
                    .clickable {
                        action?.invoke()
                    }
            )
        } else {
            Image(
                painter = actionPainter,
                contentDescription = "provided image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(actionPainterSize)
                    .clickable {
                        action?.invoke()
                    }
            )
        }
    }
}