package com.kavi.pbc.web.splash

import androidx.compose.runtime.Composable
import com.kavi.pbc.web.parent.contract.model.SplashContract
import com.kavi.pbc.web.splash.ui.SplashUI

class SplashModule: SplashContract {
    @Composable
    override fun RetrieveEntry() {
        SplashUI()
    }
}