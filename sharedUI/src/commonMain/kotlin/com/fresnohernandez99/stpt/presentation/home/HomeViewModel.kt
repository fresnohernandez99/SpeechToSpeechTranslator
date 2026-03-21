package com.fresnohernandez99.stpt.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fresnohernandez99.stpt.domain.model.Language
import io.github.hyochan.audio.AudioRecorderPlayerProperties
import io.github.hyochan.audio.RecorderAudioSet
import io.github.hyochan.audio.createAudioRecorderPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val audioRecorderPlayer = createAudioRecorderPlayer()
    private var recordedFilePath: String? = null

    init {
        setupListeners()
        loadRecordingInfo()
        // Enable metering for demo
        audioRecorderPlayer.setPlayerProperties(
            AudioRecorderPlayerProperties(
                updateIntervalMs = 25L,
                meteringEnabled = true
            )
        )
    }

    private fun setupListeners() {
        audioRecorderPlayer.addRecordingListener { progress ->
            _uiState.update { it.copy(recordTime = progress.formattedTime) }
        }

        audioRecorderPlayer.addPlaybackListener { progress ->
            val isFinished = progress.duration > 0 && progress.currentPosition >= progress.duration
            _uiState.update {
                it.copy(
                    playTime = if (isFinished) progress.formattedDuration else progress.formattedCurrentTime,
                    duration = progress.formattedDuration,
                    playProgress = if (isFinished) 1f else if (progress.duration > 0) {
                        progress.currentPosition.toFloat() / progress.duration.toFloat()
                    } else 0f,
                    isPlaying = !isFinished,
                    isPlayingPaused = if (isFinished) false else it.isPlayingPaused
                )
            }
        }

        // Add metering listener
        audioRecorderPlayer.addAudioMeteringListener { meteringInfo ->
            val normalizedLevel = if (meteringInfo.averagePower in 0f..1f) {
                meteringInfo.averagePower
            } else {
                ((meteringInfo.averagePower + 60f) / 60f).coerceIn(0f, 1f)
            }
            _uiState.update { it.copy(meteringLevel = normalizedLevel) }
        }
    }

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
            delay(1500)
            val translated = text
            _uiState.update {
                it.copy(
                    translatedText = translated,
                    isTranslating = false
                )
            }
        }
    }

    fun onRecordingStateChange(value: Boolean) {
        if (value) startRecording() else stopRecording()
    }

    fun startRecording() {
        viewModelScope.launch {
            audioRecorderPlayer.startRecording().fold(
                onSuccess = { filePath ->
                    recordedFilePath = filePath

                    println("RecordPath!!!: $filePath")

                    _uiState.update {
                        it.copy(
                            isRecording = true,
                            isRecordingPaused = false,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update { it.copy(errorMessage = "Recording failed: ${exception.message}") }
                }
            )
        }
    }

    fun stopRecording() {
        viewModelScope.launch {
            audioRecorderPlayer.stopRecording().fold(
                onSuccess = { filePath ->
                    recordedFilePath = filePath
                    _uiState.update {
                        it.copy(
                            isRecording = false,
                            isRecordingPaused = false,
                            recordTime = "00:00:00"
                        )
                    }
                    loadRecordingInfo()
                },
                onFailure = { exception ->
                    _uiState.update { it.copy(errorMessage = "Stop recording failed: ${exception.message}") }
                }
            )
        }
    }

    fun pauseRecording() {
        viewModelScope.launch {
            audioRecorderPlayer.pauseRecording().fold(
                onSuccess = {
                    _uiState.update { it.copy(isRecordingPaused = true) }
                },
                onFailure = { exception ->
                    _uiState.update { it.copy(errorMessage = "Pause recording failed: ${exception.message}") }
                }
            )
        }
    }

    fun resumeRecording() {
        viewModelScope.launch {
            audioRecorderPlayer.resumeRecording().fold(
                onSuccess = {
                    _uiState.update { it.copy(isRecordingPaused = false) }
                },
                onFailure = { exception ->
                    _uiState.update { it.copy(errorMessage = "Resume recording failed: ${exception.message}") }
                }
            )
        }
    }

    fun startPlaying() {
        viewModelScope.launch {
            audioRecorderPlayer.startPlaying(recordedFilePath).fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isPlaying = true,
                            isPlayingPaused = false,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update { it.copy(errorMessage = "Playback failed: ${exception.message}") }
                }
            )
        }
    }

    fun stopPlaying() {
        viewModelScope.launch {
            audioRecorderPlayer.stopPlaying().fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isPlaying = false,
                            isPlayingPaused = false,
                            playTime = "00:00:00",
                            playProgress = 0f
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update { it.copy(errorMessage = "Stop playback failed: ${exception.message}") }
                }
            )
        }
    }

    fun pausePlaying() {
        viewModelScope.launch {
            audioRecorderPlayer.pausePlaying().fold(
                onSuccess = {
                    _uiState.update { it.copy(isPlayingPaused = true) }
                },
                onFailure = { exception ->
                    _uiState.update { it.copy(errorMessage = "Pause playback failed: ${exception.message}") }
                }
            )
        }
    }

    fun resumePlaying() {
        viewModelScope.launch {
            audioRecorderPlayer.resumePlaying().fold(
                onSuccess = {
                    _uiState.update { it.copy(isPlayingPaused = false) }
                },
                onFailure = { exception ->
                    _uiState.update { it.copy(errorMessage = "Resume playback failed: ${exception.message}") }
                }
            )
        }
    }

    fun seekTo(position: Float) {
        if (uiState.value.duration != "00:00:00") {
            val durationMs = parseDuration(uiState.value.duration)
            val seekPositionMs = (position * durationMs).toLong()

            viewModelScope.launch {
                audioRecorderPlayer.seekTo(seekPositionMs).fold(
                    onSuccess = {},
                    onFailure = { exception ->
                        _uiState.update { it.copy(errorMessage = "Seek failed: ${exception.message}") }
                    }
                )
            }
        }
    }

    fun setVolume(volume: Float) {
        viewModelScope.launch {
            audioRecorderPlayer.setVolume(volume).fold(
                onSuccess = {},
                onFailure = { exception ->
                    _uiState.update { it.copy(errorMessage = "Set volume failed: ${exception.message}") }
                }
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun setUpdateInterval(intervalMs: Long) {
        audioRecorderPlayer.setPlayerProperties(
            AudioRecorderPlayerProperties(updateIntervalMs = intervalMs)
        )
    }

    fun setRecorderAudioSettings(audioSet: RecorderAudioSet) {
        audioRecorderPlayer.setRecorderProperties(audioSet)
    }

    private fun parseDuration(formattedTime: String): Long {
        return try {
            val parts = formattedTime.split(":")
            if (parts.size == 3) {
                val minutes = parts[0].toLong()
                val seconds = parts[1].toLong()
                val centiseconds = parts[2].toLong()
                (minutes * 60 + seconds) * 1000 + centiseconds * 10
            } else 0L
        } catch (e: Exception) {
            0L
        }
    }

    private fun loadRecordingInfo() {
        viewModelScope.launch {
            try {
                val result = audioRecorderPlayer.getRecordingInfo()
                result.fold(
                    onSuccess = { info ->
                        _uiState.update {
                            it.copy(
                                recordingInfo = info?.let { i ->
                                    "Duration: ${i.formattedDuration} | Size: ${i.formattedFileSize}"
                                }
                            )
                        }
                    },
                    onFailure = { }
                )
            } catch (e: Exception) {
            }
        }
    }

    fun setPlaybackSpeed(speed: Float) {
        viewModelScope.launch {
            audioRecorderPlayer.setPlaybackSpeed(speed).fold(
                onSuccess = {
                    _uiState.update { it.copy(playbackSpeed = speed) }
                },
                onFailure = { exception ->
                    _uiState.update { it.copy(errorMessage = "Set playback speed failed: ${exception.message}") }
                }
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioRecorderPlayer.removeListeners()
    }

    fun onUpdateRecordingPath(recordingPath: String) {
        // TODO
    }
}
