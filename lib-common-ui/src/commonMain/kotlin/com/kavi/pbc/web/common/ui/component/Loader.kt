package com.kavi.pbc.web.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun AppFullScreenLoader(
    isWithBackground: Boolean = true,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (isWithBackground) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else Color.Transparent),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}