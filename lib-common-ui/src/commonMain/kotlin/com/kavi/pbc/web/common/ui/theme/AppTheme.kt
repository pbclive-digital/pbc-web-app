package com.kavi.pbc.web.common.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

val lightTheme = lightColorScheme(
    primary = Color(0xffb84910),
    secondary = Color(0xff5c2508),
    tertiary = Color(0xfff7bea1),
    onPrimary = Color(0xfff8ede7),
    onSecondary = Color(0xffefe9e6),
    background = Color(0xfff7f1ed),
    surface = Color(0xfffefefd),
    onBackground = Color(0xff1d1b20),
    onSurface = Color(0xff404040),
    scrim = Color(0xff888888),
)

val darkTheme = lightColorScheme(
    primary = Color(0xff371505),
    secondary = Color(0xffff6b17),
    tertiary = Color(0xffd45211),
    onPrimary = Color(0xffebe8e6),
    onSecondary = Color(0xfffff0e8),
    background = Color(0xff120702),
    surface = Color(0xff020100),
    onBackground = Color(0xffe6e0e9),
    onSurface = Color(0xffffffff),
    scrim = Color(0xffffffff)
)

@Composable
fun PBCWebAppTheme(
    isDarkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val themeAdditionalColors = if (isDarkTheme) {
        ThemeAdditionalColors (
            base = Color(0xffb84910),
            default = Color(0xff000000),
            inverseDefault = Color(0xffffffff),
            quaternary = Color(0xffff6e18),
            shadow = Color(0xffffffff),
            inverseOnPrimary = Color(0xfff8ede7),
            inverseOnBackground = Color(0xff1d1b20)
        )
    } else {
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

    CompositionLocalProvider(
        LocalThemeAdditionalColors provides themeAdditionalColors
    ) {
        MaterialTheme (
            colorScheme = if (isDarkTheme) darkTheme else lightTheme,
            typography = AppTypography,
            content = content
        )
    }
}