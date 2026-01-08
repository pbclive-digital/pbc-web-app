package com.kavi.pbc.web.common.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import pbcwebapp.lib_common_ui.generated.resources.Res
import pbcwebapp.lib_common_ui.generated.resources.font_pt_sans_bold
import pbcwebapp.lib_common_ui.generated.resources.font_pt_sans_italic
import pbcwebapp.lib_common_ui.generated.resources.font_pt_sans_regular

@OptIn(ExperimentalResourceApi::class)
val PBCFontFamily: FontFamily
    @Composable
    get() = FontFamily(
        Font(Res.font.font_pt_sans_regular, style = FontStyle.Normal),
        Font(Res.font.font_pt_sans_italic, style = FontStyle.Italic),
        Font(Res.font.font_pt_sans_bold, weight = FontWeight.Bold)
    )

val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        lineHeight = 54.sp,
        letterSpacing = .5.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)