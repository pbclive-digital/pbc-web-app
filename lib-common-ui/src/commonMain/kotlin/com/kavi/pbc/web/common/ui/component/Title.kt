package com.kavi.pbc.web.common.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.kavi.pbc.web.common.ui.theme.PBCFontFamily
import org.jetbrains.compose.resources.painterResource
import pbcwebapp.lib_common_ui.generated.resources.Res
import pbcwebapp.lib_common_ui.generated.resources.icon_back_nav

@Composable
fun Title(modifier: Modifier = Modifier,
          titleText: String, textSize: Int = 48,
          textColor: Color = MaterialTheme.colorScheme.onBackground) {
    Row (
        modifier = modifier,
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
    }
}

@Composable
fun TitleWithBackNav(
    modifier: Modifier = Modifier,
    titleText: String, textSize: Int = 48,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    backAction: () -> Unit
) {
    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(Res.drawable.icon_back_nav),
            contentDescription = "Provided icon",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(48.dp)
                .clickable {
                    backAction.invoke()
                }
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = titleText,
            fontFamily = PBCFontFamily,
            fontWeight = FontWeight.Bold,
            lineHeight = textSize.sp,
            fontSize = textSize.sp,
            color = textColor
        )
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
                tint = MaterialTheme.colorScheme.primary,
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

@Composable
fun TitleWithActionComposable(modifier: Modifier = Modifier,
                    titleText: String,
                    textSize: Int = 48,
                    textColor: Color = MaterialTheme.colorScheme.onBackground,
                    content: @Composable () -> Unit) {

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

        // Action Content
        content()
    }
}