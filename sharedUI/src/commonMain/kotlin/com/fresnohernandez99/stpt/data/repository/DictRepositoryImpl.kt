package com.fresnohernandez99.stpt.data.repository

import com.fresnohernandez99.stpt.domain.model.Language
import com.fresnohernandez99.stpt.domain.repository.DictRepository
import com.fresnohernandez99.stpt.platform.DownloadStatus
import com.fresnohernandez99.stpt.platform.TranslatorManager
import kotlinx.coroutines.flow.Flow

class DictRepositoryImpl(
    private val translatorManager: TranslatorManager
): DictRepository {
    override suspend fun getDownloadedLanguages(): List<Language> {
        val codes = translatorManager.getDownloadedModels()
        return Language.list.filter { it.code in codes }
    }

    override suspend fun isLanguageDownloaded(code: String): Boolean {
        return translatorManager.checkSpecificModel(code)
    }

    override fun downloadLanguage(code: String): Flow<DownloadStatus> {
        return translatorManager.downloadSpecificModel(code)
    }

    override suspend fun deleteLanguage(code: String): Boolean {
        return translatorManager.deleteSpecificModel(code)
    }

    override suspend fun translate(text: String, source: String, target: String): String {
        return translatorManager.translateUsingModel(text, source, target)
    }
}
