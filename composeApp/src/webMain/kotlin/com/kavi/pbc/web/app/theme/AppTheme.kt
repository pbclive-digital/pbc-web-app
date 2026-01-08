package com.kavi.pbc.web.app.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val lightTheme = lightColorScheme(
    primary = Color(0xffb84910),
    secondary = Color(0xff5c2508),
    tertiary = Color(0xfff7bea1),
    onPrimary = Color(0xfff8ede7),
    onSecondary = Color(0xffefe9e6),
    background = Color(0xfff7f1ed),
    surface = Color(0xFFFFF8F3),
    onBackground = Color(0x000000),
    onSurface = Color(0x000000)
)

val darkTheme = lightColorScheme(
    primary = Color(0xffb84910),
    secondary = Color(0xff5c2508),
    tertiary = Color(0xfff7bea1),
    onPrimary = Color(0xfff8ede7),
    onSecondary = Color(0xffefe9e6),
    background = Color(0xfff7f1ed),
    surface = Color(0xFFFFF8F3),
    onBackground = Color(0x000000),
    onSurface = Color(0x000000)
)

@Composable
fun PBCWebAppTheme(
    isDarkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme (
        colorScheme = lightTheme,
        content = content
    )
}