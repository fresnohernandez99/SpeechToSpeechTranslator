package com.fresnohernandez99.stpt.presentation.transcription

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fresnohernandez99.stpt.domain.repository.PreferencesRepository
import com.fresnohernandez99.stpt.modelDownloader.ModelSelection
import com.fresnohernandez99.stpt.platform.Transcriber
import com.fresnohernandez99.stpt.presentation.transcription.textAnalysis.getSegmenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val SPACE_STR = " "

@Immutable
class TranscriptionViewModel(
    private val transcriber: Transcriber,
    private val preferencesRepository: PreferencesRepository,
    private val modelSelection: ModelSelection
) : ViewModel() {
    private val _uiState = MutableStateFlow(TranscriptionUiState())
    val uiState: StateFlow<TranscriptionUiState> = _uiState

    fun requestAudioPermission() {
        viewModelScope.launch {
            transcriber.requestRecordingPermission()
        }
    }

    fun initRecognizer() {
        viewModelScope.launch(Dispatchers.IO) {
            val modelFileName = modelSelection.getSelectedModel()
            transcriber.initialize(modelFileName.name)
        }
    }

    fun startRecognizer(filePath: String) {
        println("startRecognizer =========================")
        viewModelScope.launch(Dispatchers.Default) {
            if (transcriber.hasRecordingPermission()) {
                _uiState.update { current ->
                    current.copy(inTranscription = true)
                }
                val transcriptionLanguage =
                    preferencesRepository.getDefaultTranscriptionLanguage().first()
                val segmenter = getSegmenter(transcriptionLanguage)
                transcriber.start(
                    filePath,
                    preferencesRepository.getDefaultTranscriptionLanguage().first(),
                    onProgress = { progress ->
                        println("progress ========================= $progress")
                        _uiState.update { current ->
                            current.copy(
                                progress = progress
                            )
                        }
                    },
                    onNewSegment = { _, _, text ->
                        val delimiter =
                            if (_uiState.value.originalText.endsWith(".")) "\n\n" else SPACE_STR
                        println("\n text ========================= $text")
                        _uiState.update { current ->
                            // TODO: Verify this change
                            current.copy(
                                // originalText = "${_uiState.value.originalText}$delimiter${text.trim()}".trim(),
                                originalText = segmenter.segmentText("${_uiState.value.originalText.trim()}$delimiter${text.trim()}".trim())
                                    .joinToString("\n\n"),
                                partialText = text
                            )
                        }

                    },
                    onComplete = {
                        println("\n completed ========================= ")
                        println("\n completed ========================= ")
                        _uiState.update { current ->
                            current.copy(
                                inTranscription = false
                            )
                        }
                    },
                    onError = {
                        println("\n error ========================= ")
                        println("\n error ========================= ")
                        _uiState.update { current ->
                            current.copy(
                                inTranscription = false,
                                progress = 100,
                                hasError = true
                            )
                        }
                    })
            }
        }

    }

    fun stopRecognizer() {
        _uiState.update { current ->
            current.copy(inTranscription = false)
        }
        viewModelScope.launch {
            transcriber.stop()
        }

    }

    fun finishRecognizer() {
        _uiState.update { current ->
            current.copy(
                inTranscription = false,
                originalText = "",
                finalText = "",
                partialText = "",
                summarizedText = ""
            )
        }
        viewModelScope.launch {
            transcriber.finish()
        }
    }

    override fun onCleared() {
        stopRecognizer()
    }
}
