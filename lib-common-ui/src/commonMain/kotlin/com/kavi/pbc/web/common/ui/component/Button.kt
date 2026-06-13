package com.kavi.pbc.web.common.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kavi.pbc.web.common.ui.model.IconSide
import com.kavi.pbc.web.common.ui.theme.LocalThemeAdditionalColors
import org.jetbrains.compose.resources.painterResource
import pbcwebapp.lib_common_ui.generated.resources.Res
import pbcwebapp.lib_common_ui.generated.resources.icon_calendar
import pbcwebapp.lib_common_ui.generated.resources.icon_clock

@Composable
fun AppFilledButton(label: String,
                    labelTextSize: TextUnit? = null,
                    isDisable: MutableState<Boolean> = mutableStateOf(false),
                    modifier: Modifier = Modifier,
                    buttonHeight: Dp = 50.dp,
                    onClick: () -> Unit) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

    Button(
        onClick = onClick,
        modifier = modifier.height(buttonHeight),
        enabled = !isDisable.value,
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary, 
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = themeAdditionalColors.shadow
        )
    ) {
        Text(
            text = label.uppercase(),
            fontSize = labelTextSize ?: run { 14.sp },
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Normal,
            //modifier = Modifier.padding(4.dp),
        )
    }
}

@Composable
fun AppButtonWithIcon(label: String,
                      icon: Painter,
                      buttonHeight: Dp = 50.dp,
                      cornerRadius: Dp = 5.dp,
                      iconSide: IconSide = IconSide.LEFT,
                      labelTextSize: TextUnit? = null,
                      modifier: Modifier = Modifier,
                      onClick: () -> Unit) {

    Button(
        onClick = onClick,
        modifier = modifier.height(buttonHeight),
        shape = RoundedCornerShape(cornerRadius),
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (iconSide) {
                IconSide.LEFT -> {
                    Icon(
                        painter = icon,
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                            .padding(4.dp)
                    )
                    Text(
                        text = label.uppercase(),
                        fontSize = labelTextSize ?: run { 14.sp },
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(4.dp),
                    )
                }
                IconSide.RIGHT -> {
                    Text(
                        text = label.uppercase(),
                        fontSize = labelTextSize ?: run { 14.sp },
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(4.dp),
                    )
                    Icon(
                        painter = icon,
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AppDatePickerButton(
    label: MutableState<String>,
    labelTextSize: TextUnit? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit) {

    val themeAdditionalColors = LocalThemeAdditionalColors.current

    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(50.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = themeAdditionalColors.quaternary,
        )
    ) {
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label.value,
                fontSize = labelTextSize ?: run { 16.sp },
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(4.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(Res.drawable.icon_calendar),
                contentDescription = "Calendar icon",
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun AppTimePickerButton(
    label: MutableState<String>,
    labelTextSize: TextUnit? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit) {

    val themeAdditionalColors = LocalThemeAdditionalColors.current

    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(50.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = themeAdditionalColors.quaternary,
        )
    ) {
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label.value,
                fontSize = labelTextSize ?: run { 16.sp },
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(4.dp),
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(Res.drawable.icon_clock),
                contentDescription = "Calendar icon",
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun AppDisabledFilledButton(label: String,
                            labelTextSize: TextUnit? = null,
                            modifier: Modifier,
                            onClick: (() -> Unit)? = null) {
    Button(
        onClick = onClick ?: run {{ /*Empty click event*/ }},
        modifier = modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(5.dp),
        //colors = ButtonDefaults.buttonColors(contentColor = grayText, containerColor = grayLight)
    ) {
        Text(
            text = label.uppercase(),
            fontSize = labelTextSize ?: run { 14.sp },
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(4.dp),
        )
    }
}

@Composable
fun AppOutlineButton(label: String,
                     labelTextSize: TextUnit? = null,
                     modifier: Modifier = Modifier,
                     onClick: () -> Unit) {
    val themeAdditionalColors = LocalThemeAdditionalColors.current

    OutlinedButton(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(50.dp),
        border = BorderStroke(1.dp, themeAdditionalColors.quaternary),
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = themeAdditionalColors.quaternary,
        )
    ) {
        Text(
            text = label.uppercase(),
            color = themeAdditionalColors.quaternary,
            fontSize = labelTextSize ?: run { 14.sp },
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(4.dp),
        )
    }
}

@Composable
fun AppDisabledOutlineButton(label: String,
                             labelTextSize: TextUnit? = null,
                             modifier: Modifier,
                             onClick: (() -> Unit)? = null) {
    OutlinedButton(
        onClick = onClick ?: run {{ /*Empty click event*/ }},
        modifier = modifier.fillMaxWidth().height(50.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.outline)
    ) {
        Text(
            text = label.uppercase(),
            fontSize = labelTextSize ?: run { 14.sp },
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(4.dp),
        )
    }
}

@Composable
fun AppIconButton(
    icon: Painter,
    buttonSize: Dp = 50.dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val iconSize: Int = (buttonSize.value * 0.75).toInt()

    Box (
        modifier = modifier
            .width(buttonSize).height(buttonSize)
            .clip(shape = RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary)
            .clickable {
                onClick.invoke()
            },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = icon,
            tint = Color.White,
            contentDescription = "Provided icon",
            modifier = Modifier
                .size(iconSize.dp)
                .padding(4.dp)
        )
    }
}

@Composable
fun AppLinkButton(
    label: String,
    labelTextSize: TextUnit? = null,
    fontFamily: FontFamily? = null,
    color: Color? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Text(
        text = label,
        fontSize = labelTextSize ?: run { 16.sp },
        fontFamily = fontFamily,
        color = color ?: run { MaterialTheme.colorScheme.primary },
        style = TextStyle(textDecoration = TextDecoration.Underline),
        modifier = modifier
            .clickable {
                onClick.invoke()
            },
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewUi() {
    Column {
        AppDisabledOutlineButton("Button Text", modifier = Modifier) {}

        AppLinkButton("Button Text Link", ) {}
    }
}