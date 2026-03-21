package com.fresnohernandez99.stpt.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.fresnohernandez99.stpt.domain.model.Language
import com.fresnohernandez99.stpt.modelDownloader.NO_MODEL_SELECTION
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        private val KEY_ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        private val KEY_LANGUAGE = stringPreferencesKey("language")
        private val KEY_MODEL_DOWNLOAD_ID = longPreferencesKey("model_download_id")
        private val KEY_MODEL_SELECTION = intPreferencesKey("model_selection")
    }

    suspend fun hasCompletedOnboarding(): Boolean {
        return dataStore.data.first()[KEY_ONBOARDING_COMPLETED] ?: false
    }


    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { prefs ->
            prefs[KEY_ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun setDefaultTranscriptionLanguage(language: String) {
        dataStore.edit { prefs ->
            prefs[KEY_LANGUAGE] = language
        }
    }

    fun getDefaultTranscriptionLanguage(): Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_LANGUAGE] ?: Language.Spanish.code
    }

    fun getModelDownloadId(): Flow<Long> = dataStore.data.map { prefs ->
        prefs[KEY_MODEL_DOWNLOAD_ID] ?: -1
    }

    suspend fun setModelDownloadId(downloadId: Long) {
        dataStore.edit { prefs ->
            prefs[KEY_MODEL_DOWNLOAD_ID] = downloadId
        }
    }

    fun getModelSelection(): Flow<Int> = dataStore.data.map { prefs ->
        prefs[KEY_MODEL_SELECTION] ?: NO_MODEL_SELECTION
    }

    suspend fun setModelSelection(modelSelection: Int) {
        dataStore.edit { prefs ->
            prefs[KEY_MODEL_SELECTION] = modelSelection
        }
    }
}

