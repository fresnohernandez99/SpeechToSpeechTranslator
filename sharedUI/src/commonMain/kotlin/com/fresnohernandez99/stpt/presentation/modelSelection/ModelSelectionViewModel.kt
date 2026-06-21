package com.fresnohernandez99.stpt.presentation.modelSelection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fresnohernandez99.stpt.domain.repository.PreferencesRepository
import kotlinx.coroutines.launch

class ModelSelectionViewModel(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    val language = preferencesRepository.getDefaultTranscriptionLanguage()
    val modelSavedSelection = preferencesRepository.getModelSelection()

    fun setModelSelection(selection: Int) {
        viewModelScope.launch {
            preferencesRepository.setModelSelection(selection)
        }
    }
}
