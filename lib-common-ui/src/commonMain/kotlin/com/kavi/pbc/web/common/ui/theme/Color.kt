package com.kavi.pbc.web.common.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class ThemeAdditionalColors(
    val base: Color,
    val default: Color,
    val inverseDefault: Color,
    val quaternary: Color,
    val shadow: Color,
    val inverseOnPrimary: Color,
    val inverseOnBackground: Color
)

val LocalThemeAdditionalColors = staticCompositionLocalOf {
    ThemeAdditionalColors (
        base = Color(0xffb84910),
        default = Color(0xffffffff),
        inverseDefault = Color(0xff000000),
        quaternary = Color(0xffb84910),
        shadow = Color(0xff888888),
        inverseOnPrimary = Color(0xffebe8e6),
        inverseOnBackground = Color(0xffe6e0e9)
    )
}