package com.fresnohernandez99.stpt.platform

expect class LanguageIdManager {
    suspend fun getLanguage(text: String): String
}

interface LanguageIdManagerIos {
    suspend fun getLanguage(
        text: String
    ): String
}
