package com.fresnohernandez99.stpt.platform

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

actual class TranslatorManager {

    private val modelManager = RemoteModelManager.getInstance()

    actual suspend fun getDownloadedModels(): List<String> {
        return try {
            val models = modelManager.getDownloadedModels(TranslateRemoteModel::class.java).await()
            models.map { it.language }
        } catch (e: Exception) {
            emptyList()
        }
    }

    actual suspend fun checkSpecificModel(languageCode: String): Boolean {
        val model = TranslateRemoteModel.Builder(languageCode).build()
        return modelManager.isModelDownloaded(model).await()
    }

    actual fun downloadSpecificModel(languageCode: String): Flow<DownloadStatus> = callbackFlow {
        val model = TranslateRemoteModel.Builder(languageCode).build()
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        trySend(DownloadStatus.Downloading)

        modelManager.download(model, conditions)
            .addOnSuccessListener {
                trySend(DownloadStatus.Success)
                close()
            }
            .addOnFailureListener { e ->
                trySend(DownloadStatus.Error(e.message ?: "Unknown error downloading model"))
                close(e)
            }

        awaitClose { /* Cleanup if necessary */ }
    }

    actual suspend fun deleteSpecificModel(languageCode: String): Boolean {
        val model = TranslateRemoteModel.Builder(languageCode).build()
        return try {
            modelManager.deleteDownloadedModel(model).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    actual suspend fun translateUsingModel(
        text: String,
        sourceLanguage: String,
        targetLanguage: String
    ): String {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguage)
            .setTargetLanguage(targetLanguage)
            .build()

        val translator = Translation.getClient(options)

        return try {
            val result = translator.translate(text).await()
            result
        } catch (e: Exception) {
            "Translation Error: ${e.message}"
        } finally {
            translator.close()
        }
    }
}
