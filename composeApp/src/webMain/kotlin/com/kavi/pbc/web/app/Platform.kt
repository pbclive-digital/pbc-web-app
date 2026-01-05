package com.kavi.pbc.web.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform