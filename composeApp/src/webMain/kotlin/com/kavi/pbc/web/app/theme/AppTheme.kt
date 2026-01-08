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
    surface = Color(0xfffefefd),
    onBackground = Color(0xff1d1b20),
    onSurface = Color(0xff404040),
    scrim = Color(0xff888888)
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
    MaterialTheme (
        colorScheme = lightTheme,
        content = content
    )
}