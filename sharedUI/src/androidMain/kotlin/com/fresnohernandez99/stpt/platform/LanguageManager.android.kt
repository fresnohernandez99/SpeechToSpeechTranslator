package com.fresnohernandez99.stpt.platform

import com.google.mlkit.nl.languageid.LanguageIdentification
import kotlinx.coroutines.tasks.await

actual class LanguageIdManager {

    private val languageIdentifier = LanguageIdentification.getClient()

    actual suspend fun getLanguage(
        text: String
    ): String {
        return try {
            val result = languageIdentifier.identifyLanguage(text).await()
            result
        } catch (e: Exception) {
            "Get language Error: ${e.message}"
        } finally {
            languageIdentifier.close()
        }
    }
}
