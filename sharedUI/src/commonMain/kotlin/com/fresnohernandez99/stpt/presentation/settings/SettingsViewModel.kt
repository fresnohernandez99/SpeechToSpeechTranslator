package com.fresnohernandez99.stpt.presentation.settings

import androidx.lifecycle.ViewModel
import com.fresnohernandez99.stpt.data.repository.PreferencesRepository

class SettingsViewModel(
    val preferencesRepository: PreferencesRepository
) : ViewModel() {
    val language = preferencesRepository.getDefaultTranscriptionLanguage()
    val modelSavedSelection = preferencesRepository.getModelSelection()
}
