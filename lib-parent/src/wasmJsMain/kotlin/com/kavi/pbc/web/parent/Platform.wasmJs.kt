package com.kavi.pbc.web.parent

import kotlinx.browser.window

actual fun openUrlInNewTab(url: String) {
    window.open(url, "_blank")
}