package com.fresnohernandez99.stpt.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fresnohernandez99.stpt.domain.model.Language
import com.fresnohernandez99.stpt.domain.repository.PreferencesRepository
import com.fresnohernandez99.stpt.modelDownloader.FARSI
import com.fresnohernandez99.stpt.modelDownloader.OPTIMIZED_MODEL_SELECTION
import kotlinx.coroutines.launch

class SettingsViewModel(
    val preferencesRepository: PreferencesRepository
) : ViewModel() {
    val language = preferencesRepository.getDefaultTranscriptionLanguage()
    val modelSavedSelection = preferencesRepository.getModelSelection()

    fun onLanguageSelected(languageEntry: Language) {
        viewModelScope.launch {
            preferencesRepository.setDefaultTranscriptionLanguage(languageEntry.code)
            if (languageEntry.code == FARSI) {
                preferencesRepository.setModelSelection(OPTIMIZED_MODEL_SELECTION)
            }
        }
    }
}
