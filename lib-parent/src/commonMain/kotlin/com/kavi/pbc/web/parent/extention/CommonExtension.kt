package com.kavi.pbc.web.parent.extention

import com.kavi.pbc.web.parent.openMapsWithUrl
import com.kavi.pbc.web.parent.openUrlInNewTab

fun openUrl(url: String){
    openUrlInNewTab(url = url)
}

fun openMaps(mapUrl: String) {
    openMapsWithUrl(mapUrl = mapUrl)
}

fun String.copy(): String {
    return this
}