package com.fresnohernandez99.stpt.presentation.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fresnohernandez99.stpt.domain.model.Language
import com.fresnohernandez99.stpt.domain.repository.DictRepository
import com.fresnohernandez99.stpt.domain.repository.TranslationHistoryRepository
import com.fresnohernandez99.stpt.platform.DownloadStatus
import dev.theolm.record.Record
import dev.theolm.record.config.AudioEncoder
import dev.theolm.record.config.OutputFormat
import dev.theolm.record.config.OutputLocation
import dev.theolm.record.config.RecordConfig
import io.github.hyochan.audio.AudioRecorderPlayerProperties
import io.github.hyochan.audio.RecorderAudioSet
import io.github.hyochan.audio.createAudioRecorderPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Immutable
class HomeViewModel(
    private val dictRepository: DictRepository,
    private val translationHistoryRepository: TranslationHistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val audioRecorderPlayer = createAudioRecorderPlayer()
    private var recordedFilePath: String? = null
    private var timerJob: Job? = null

    private var _last3 = MutableStateFlow<HistoryState>(HistoryState.Loading)
    val last3 = _last3.asStateFlow()

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

        loadLast3()

        //outputLocation = OutputLocation.Cache,
//                    outputFormat = OutputFormat.WAV,
//                    audioEncoder = AudioEncoder.PCM_16BIT,
//                    sampleRate = 16000
//        audioRecorderPlayer.setRecorderProperties(
//            RecorderAudioSet(
//                avSampleRateKeyIOS = TODO(),
//                avFormatIDKeyIOS = TODO(),
//                avNumberOfChannelsKeyIOS = TODO(),
//                avEncoderAudioQualityKeyIOS = TODO(),
//                avLinearPCMBitDepthKeyIOS = TODO(),
//                avLinearPCMIsBigEndianKeyIOS = TODO(),
//                avLinearPCMIsFloatKeyIOS = TODO(),
//                avLinearPCMIsNonInterleavedIOS = TODO(),
//                avEncoderBitRateKeyIOS = TODO(),
//                audioSourceAndroid = TODO(),
//                outputFormatAndroid = TODO(),
//                audioEncoderAndroid = TODO(),
//                audioEncodingBitRateAndroid = TODO(),
//                audioSamplingRateAndroid = TODO(),
//                audioChannelsAndroid = TODO()
//            )
//        )
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
        _uiState.update {
            it.copy(
                textToTranslate = text,
                translateState = TranslateState.NOT_REQUESTED
            )
        }
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

        val source = uiState.value.sourceLanguage
        val target = uiState.value.targetLanguage

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    translateState = TranslateState.LOADING
                )
            }

            // Si el origen es auto-detect, ML Kit lo maneja internamente en el translate si el modelo base está
            // Pero para offline, necesitamos asegurar que el modelo de destino esté descargado
            // Y si el origen es específico, también.

            if (source != Language.Detect) {
                downloadIfNeeded(source.code)
            }
            downloadIfNeeded(target.code)

            try {
                val translated = dictRepository.translate(text, source.code, target.code)
                _uiState.update {
                    it.copy(
                        translatedText = translated,
                        translateState = TranslateState.SUCCESS
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        translateState = TranslateState.ERROR,
                        errorMessage = e.message ?: "Translation failed"
                    )
                }
            }
        }
    }

    private suspend fun downloadIfNeeded(code: String) {
        if (!dictRepository.isLanguageDownloaded(code)) {
            dictRepository.downloadLanguage(code).collect { status ->
                when (status) {
                    is DownloadStatus.Downloading -> {
                        _uiState.update {
                            it.copy(
                                isDownloading = true,
                                downloadProgress = "Downloading $code..."
                            )
                        }
                    }

                    is DownloadStatus.Success -> {
                        _uiState.update { it.copy(isDownloading = false, downloadProgress = "") }
                    }

                    is DownloadStatus.Error -> {
                        _uiState.update {
                            it.copy(
                                isDownloading = false,
                                errorMessage = status.message
                            )
                        }
                    }
                }
            }
        }
    }

    fun onRecordingStateChange(value: Boolean) {
        if (value) startRecording() else {
        }//stopRecording()
    }

    fun startRecording() {
        viewModelScope.launch {
            Record.setConfig(
                RecordConfig(
                    outputLocation = OutputLocation.Cache,
                    outputFormat = OutputFormat.WAV,
                    audioEncoder = AudioEncoder.PCM_16BIT,
                    sampleRate = 16000
                )
            )
            try {
                Record.startRecording()
                _uiState.update {
                    it.copy(
                        isRecording = true,
                        isRecordingPaused = false,
                        errorMessage = null,
                        recordTime = "00:00:00"
                    )
                }

                timerJob?.cancel()
                timerJob = launch {
                    var seconds = 0L
                    while (true) {
                        delay(1000.milliseconds)
                        seconds++
                        val formatted = formatSeconds(seconds)
                        _uiState.update { it.copy(recordTime = formatted) }
                    }
                }
            } catch (e: Exception) {
                // TODO handle errors
            }

//            audioRecorderPlayer.startRecording().fold(
//                onSuccess = { filePath ->
//                    recordedFilePath = filePath
//
//                    println("RecordPath!!!: $filePath")
//
//                    _uiState.update {
//                        it.copy(
//                            isRecording = true,
//                            isRecordingPaused = false,
//                            errorMessage = null
//                        )
//                    }
//                },
//                onFailure = { exception ->
//                    _uiState.update { it.copy(errorMessage = "Recording failed: ${exception.message}") }
//                }
//            )
        }
    }

    private fun formatSeconds(seconds: Long): String {
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60
        return "${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}:${
            s.toString().padStart(2, '0')
        }"
    }

    fun stopRecording() {
        viewModelScope.launch {
            try {
                val savedAudioPath = Record.stopRecording()
                timerJob?.cancel()
                recordedFilePath = savedAudioPath

                _uiState.update {
                    it.copy(
                        isRecording = false,
                        duration = it.recordTime
                    )
                }
                println("Recording stopped. File saved at $savedAudioPath")
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al detener grabación: ${e.message}") }
            }

//            audioRecorderPlayer.stopRecording().fold(
//                onSuccess = { filePath ->
//                    recordedFilePath = filePath
//
//                    _uiState.update {
//                        it.copy(
//                            isRecording = false,
//                            isRecordingPaused = false,
//                            recordTime = "00:00:00"
//                        )
//                    }
//                    loadRecordingInfo()
//                },
//                onFailure = { exception ->
//                    _uiState.update { it.copy(errorMessage = "Stop recording failed: ${exception.message}") }
//                }
//            )
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

    fun loadLast3() {
        viewModelScope.launch(Dispatchers.IO) {
            val dataInDb = translationHistoryRepository.getLast3()
            delay(2000.milliseconds)
            if (dataInDb.isEmpty()) {
                _last3.value = HistoryState.Empty
            } else {
                _last3.value = HistoryState.Success(dataInDb)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioRecorderPlayer.removeListeners()
        timerJob?.cancel()
    }

    fun onCompletedRecording(onFileFound: (String) -> Unit) {
        onFileFound(recordedFilePath ?: "")
    }
}
