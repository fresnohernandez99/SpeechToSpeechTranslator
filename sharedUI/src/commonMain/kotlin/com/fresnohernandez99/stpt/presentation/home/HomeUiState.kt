package com.fresnohernandez99.stpt.presentation.home

import com.fresnohernandez99.stpt.domain.model.Language

data class HomeUiState(
    val textToTranslate: String = "",
    val sourceLanguage: Language = Language.Detect,
    val targetLanguage: Language = Language.Spanish,
    val translatedText: String = "",
    val isTranslating: Boolean = false,
    val isRecording: Boolean = false
)
