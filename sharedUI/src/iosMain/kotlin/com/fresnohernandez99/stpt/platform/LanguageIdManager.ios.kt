package com.fresnohernandez99.stpt.platform

actual class LanguageIdManager(private val languageIdManagerIos: LanguageIdManagerIos) {
    actual suspend fun getLanguage(
        text: String,
    ): String {
        return languageIdManagerIos.getLanguage(
            text = text
        )
    }
}
