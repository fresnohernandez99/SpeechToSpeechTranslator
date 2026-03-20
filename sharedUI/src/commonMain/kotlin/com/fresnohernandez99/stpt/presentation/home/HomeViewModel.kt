package com.fresnohernandez99.stpt.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fresnohernandez99.stpt.domain.model.Language
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    fun onTextChanged(text: String) {
        _uiState.update { it.copy(textToTranslate = text) }
    }

    fun onSourceLanguageSelected(language: Language) {
        _uiState.update { it.copy(sourceLanguage = language) }
    }

    fun onTargetLanguageSelected(language: Language) {
        _uiState.update { it.copy(targetLanguage = language) }
    }

    fun translate() {
        val text = uiState.value.textToTranslate
        if (text.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isTranslating = true) }
            
            // Mock de procesamiento
            delay(1500)
            
            // TODO: Iniciar la llamada al método que traduce aquí (Interactor/UseCase)
            val translated = text // Fake: devuelve el mismo texto
            
            _uiState.update { 
                it.copy(
                    translatedText = translated,
                    isTranslating = false
                ) 
            }
        }
    }
}
