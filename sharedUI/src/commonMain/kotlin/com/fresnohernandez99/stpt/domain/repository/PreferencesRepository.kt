package com.fresnohernandez99.stpt.domain.repository

import com.fresnohernandez99.stpt.domain.model.LanguagesInPref
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    suspend fun hasCompletedOnboarding(): Boolean

    suspend fun setOnboardingCompleted(completed: Boolean)

    suspend fun setDefaultTranscriptionLanguage(language: String)

    fun getDefaultTranscriptionLanguage(): Flow<String>

    fun getModelDownloadId(): Flow<Long>

    suspend fun setModelDownloadId(downloadId: Long)

    fun getModelSelection(): Flow<Int>

    suspend fun setModelSelection(modelSelection: Int)

    fun getLanguagePref(): Flow<LanguagesInPref>

    suspend fun setLanguagePref(pref: LanguagesInPref)
}
