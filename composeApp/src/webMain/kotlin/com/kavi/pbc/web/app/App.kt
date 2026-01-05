package com.kavi.pbc.web.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kavi.pbc.web.parent.contract.ContractServiceLocator
import com.kavi.pbc.web.parent.contract.model.SplashContract

@Composable
fun App() {
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

    MaterialTheme (
        colorScheme = lightTheme
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
        ) {
            ContractServiceLocator.locate(SplashContract::class).RetrieveEntry()
            //ContractServiceLocator.locate(DashboardContract::class).RetrieveEntry()
        }
    }
}