package com.fresnohernandez99.stpt.presentation.languageSelection

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fresnohernandez99.stpt.domain.model.Language
import com.fresnohernandez99.stpt.domain.repository.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Immutable
class LanguageSelectionViewModel(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    val selectedLanguage = preferencesRepository.getLanguagePref()

    private var isSelecting = false

    fun changeSelection(language: Language, isSource: Boolean, onCompleted: () -> Unit) {
        if (!isSelecting) {
            isSelecting = true
            viewModelScope.launch(Dispatchers.IO) {
                preferencesRepository.setLanguagePref(
                    if (isSource) {
                        selectedLanguage.first().copy(sourceLanguage = language)
                    } else {
                        selectedLanguage.first().copy(targetLanguage = language)
                    }
                )
                delay(600.milliseconds)
                viewModelScope.launch(Dispatchers.Main) {
                    onCompleted()
                    isSelecting = false
                }
            }
        }
    }
}
