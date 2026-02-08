package com.kavi.pbc.web.parent.extention

import com.kavi.pbc.web.parent.openUrlInNewTab

fun openUrl(url: String){
    openUrlInNewTab(url = url)
}

fun String.copy(): String {
    return this
}