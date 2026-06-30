package com.fresnohernandez99.stpt.presentation.home

import androidx.compose.runtime.Immutable
import com.fresnohernandez99.stpt.data.local.TranslatedItem
import com.fresnohernandez99.stpt.domain.model.Language

@Immutable
data class HomeUiState(
    val textToTranslate: String = "",
    val translatedText: String = "",
    val translateState: TranslateState = TranslateState.NOT_REQUESTED,
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

enum class TranslateState {
    NOT_REQUESTED, LOADING, SUCCESS, ERROR
}

sealed class HistoryState {
    data class Success(val items: List<TranslatedItem> = listOf()) : HistoryState()
    object Loading : HistoryState()
    object Empty : HistoryState()
}
