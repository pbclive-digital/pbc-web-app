package com.kavi.pbc.web.parent.extention

import com.kavi.pbc.web.parent.openUrlInNewTab

fun openUrl(url: String, closeBlankPage: Boolean = false){
    openUrlInNewTab(url = url, closeBlankPage = closeBlankPage)
}

fun String.copy(): String {
    return this
}