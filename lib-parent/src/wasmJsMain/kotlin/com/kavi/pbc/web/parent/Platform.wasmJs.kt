package com.kavi.pbc.web.parent

import kotlinx.browser.window

@OptIn(ExperimentalWasmJsInterop::class)
actual fun openUrlInNewTab(url: String, closeBlankPage: Boolean) {
    val newTab = window.open(url, "_blank")

    if (closeBlankPage) {
        if (newTab != null) {
            window.setTimeout({
                try {
                    newTab.closed
                } catch (e: Exception) {
                    println("Could not close tab: ${e.message}")
                } as JsAny?
            }, 500)
        }
    }
}