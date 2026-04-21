package com.kavi.pbc.web.parent

import kotlinx.browser.window

actual fun openUrlInNewTab(url: String) {
    window.open(url, "_blank")
}

actual fun openMapsWithUrl(mapUrl: String) {
    val newTab = window.open(mapUrl, "_blank")
    if (newTab != null) {
        window.setTimeout({
            try {
                newTab.close()
            } catch (e: Exception) {
                println("Could not close tab: ${e.message}")
            } as? JsAny
        }, 500)
    }
}