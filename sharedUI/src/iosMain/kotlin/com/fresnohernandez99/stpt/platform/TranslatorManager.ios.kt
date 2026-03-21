package com.fresnohernandez99.stpt.platform

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

actual class TranslatorManager(private val translatorManagerIos: TranslatorManagerIos) {

    actual suspend fun getDownloadedModels(): List<String> {
        return try {
            val models = translatorManagerIos.getDownloadedModels()
            models
        } catch (e: Exception) {
            emptyList()
        }
    }

    actual suspend fun checkSpecificModel(languageCode: String): Boolean {
        val isModelDownloaded = translatorManagerIos.checkSpecificModel(languageCode)
        return isModelDownloaded
    }

    actual fun downloadSpecificModel(languageCode: String): Flow<DownloadStatus> = callbackFlow {
        translatorManagerIos.downloadSpecificModel(languageCode) {
            when (it) {
                DownloadStatus.Downloading -> trySend(DownloadStatus.Downloading)
                is DownloadStatus.Error -> {
                    trySend(DownloadStatus.Success)
                    close()
                }

                DownloadStatus.Success -> {
                    trySend(DownloadStatus.Success)
                    close()
                }
            }
        }

        awaitClose { /* Cleanup if necessary */ }
    }

    actual suspend fun deleteSpecificModel(languageCode: String): Boolean {
        return translatorManagerIos.deleteSpecificModel(languageCode)
    }

    actual suspend fun translateUsingModel(
        text: String,
        sourceLanguage: String,
        targetLanguage: String
    ): String {
        return translatorManagerIos.translateUsingModel(
            text = text,
            sourceLanguage = sourceLanguage,
            targetLanguage = targetLanguage
        )
    }
}
