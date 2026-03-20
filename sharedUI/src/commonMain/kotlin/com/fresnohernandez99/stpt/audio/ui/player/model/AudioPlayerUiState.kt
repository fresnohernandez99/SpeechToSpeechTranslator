package com.fresnohernandez99.stpt.audio.ui.player.model

data class AudioPlayerUiState(
    val isLoaded: Boolean,
    val isPlaying: Boolean,
    val currentPosition: Int,
    val duration: Int,
    val errorMessage: String
)
