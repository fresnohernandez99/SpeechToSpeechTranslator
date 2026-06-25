package com.fresnohernandez99.stpt.platform

import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import kotlinx.coroutines.tasks.await

actual class LanguageIdManager {
    val options = LanguageIdentificationOptions.Builder()
        .setConfidenceThreshold(0.01f)
        .build()

    private val languageIdentifier = LanguageIdentification.getClient(options)

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
