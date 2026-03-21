package com.fresnohernandez99.stpt.presentation.home

import com.fresnohernandez99.stpt.domain.model.Language

data class HomeUiState(
    val textToTranslate: String = "",
    val sourceLanguage: Language = Language.Detect,
    val targetLanguage: Language = Language.Spanish,
    val translatedText: String = "",
    val isTranslating: Boolean = false,
    val isRecording: Boolean = false,
    val recordTime: String = "00:00:00",
    val playTime: String = "00:00:00",
    val duration: String = "00:00:00",
    val playProgress: Float = 0f,
    val isPlaying: Boolean = false,
    val isRecordingPaused: Boolean = false,
    val isPlayingPaused: Boolean = false,
    val errorMessage: String? = null,
    val recordingInfo: String? = null,
    val meteringLevel: Float = 0f,
    val playbackSpeed: Float = 1.0f,
    val isDownloading: Boolean = false,
    val downloadProgress: String = ""
)
