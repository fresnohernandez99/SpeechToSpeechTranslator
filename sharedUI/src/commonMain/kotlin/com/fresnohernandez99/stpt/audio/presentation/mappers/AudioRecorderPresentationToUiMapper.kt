package com.fresnohernandez99.stpt.audio.presentation.mappers

import com.fresnohernandez99.stpt.audio.domain.AudioRecorderPresentationState
import com.fresnohernandez99.stpt.audio.ui.recorder.AudioRecorderUiState

class AudioRecorderPresentationToUiMapper {
    fun mapToUiState(presentationState: AudioRecorderPresentationState): AudioRecorderUiState {
        return AudioRecorderUiState(
            recordCounterString = presentationState.recordCounterString,
            recordingPath = presentationState.recordingPath,
            isRecordPaused = presentationState.isRecordPaused
        )
    }
}
