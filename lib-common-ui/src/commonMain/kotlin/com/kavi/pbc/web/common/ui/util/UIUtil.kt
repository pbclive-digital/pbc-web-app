package com.kavi.pbc.web.common.ui.util

import androidx.compose.ui.unit.Dp

enum class ScreenType {
    PHONE, TABLET, COMPUTER
}

object UIUtil {
    fun screenType(maxWidth: Dp): ScreenType {
        return if (maxWidth.value <= 500) {
            ScreenType.PHONE
        } else if (maxWidth.value > 500 && maxWidth.value <= 1000) {
            ScreenType.TABLET
        } else {
            ScreenType.COMPUTER
        }
    }
}