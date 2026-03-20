package com.fresnohernandez99.stpt.audio.presentation.mappers

import com.fresnohernandez99.stpt.audio.presentation.AudioPlayerPresentationState
import com.fresnohernandez99.stpt.audio.ui.player.model.AudioPlayerUiState

class AudioPlayerPresentationToUiMapper {
    fun mapToUiState(presentationState: AudioPlayerPresentationState): AudioPlayerUiState {
        return AudioPlayerUiState(
            isLoaded = presentationState.isLoaded,
            isPlaying = presentationState.isPlaying,
            currentPosition = presentationState.currentPosition,
            duration = presentationState.duration,
            errorMessage = presentationState.errorMessage.orEmpty()
        )
    }
}
