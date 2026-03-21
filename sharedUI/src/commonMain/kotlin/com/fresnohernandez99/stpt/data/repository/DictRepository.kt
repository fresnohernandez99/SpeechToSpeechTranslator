package com.fresnohernandez99.stpt.data.repository

import com.fresnohernandez99.stpt.domain.model.Language
import com.fresnohernandez99.stpt.platform.DownloadStatus
import com.fresnohernandez99.stpt.platform.TranslatorManager
import kotlinx.coroutines.flow.Flow

class DictRepository(
    private val translatorManager: TranslatorManager
) {
    suspend fun getDownloadedLanguages(): List<Language> {
        val codes = translatorManager.getDownloadedModels()
        return Language.list.filter { it.code in codes }
    }

    suspend fun isLanguageDownloaded(code: String): Boolean {
        return translatorManager.checkSpecificModel(code)
    }

    fun downloadLanguage(code: String): Flow<DownloadStatus> {
        return translatorManager.downloadSpecificModel(code)
    }

    suspend fun deleteLanguage(code: String): Boolean {
        return translatorManager.deleteSpecificModel(code)
    }
}
