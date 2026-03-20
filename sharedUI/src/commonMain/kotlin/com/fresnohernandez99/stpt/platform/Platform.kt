package com.fresnohernandez99.stpt.platform

interface Platform {
    val name: String
    val isAndroid: Boolean
    val appVersion: String
    val isTablet: Boolean
    val isLandscape: Boolean
}

expect fun getPlatform(): Platform

expect fun isDebugMode(): Boolean
