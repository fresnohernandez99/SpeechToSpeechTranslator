package com.fresnohernandez99.stpt.domain.repository

import com.fresnohernandez99.stpt.domain.model.Language
import com.fresnohernandez99.stpt.platform.DownloadStatus
import kotlinx.coroutines.flow.Flow

interface DictRepository {
    suspend fun getDownloadedLanguages(): List<Language>

    suspend fun isLanguageDownloaded(code: String): Boolean

    fun downloadLanguage(code: String): Flow<DownloadStatus>

    suspend fun deleteLanguage(code: String): Boolean

    suspend fun translate(text: String, source: String, target: String): String
}