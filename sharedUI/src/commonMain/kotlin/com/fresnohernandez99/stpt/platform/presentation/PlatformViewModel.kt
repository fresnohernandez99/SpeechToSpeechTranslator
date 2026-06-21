package com.fresnohernandez99.stpt.platform.presentation

import androidx.lifecycle.ViewModel
import com.fresnohernandez99.stpt.domain.repository.PreferencesRepository
import com.fresnohernandez99.stpt.platform.Platform
import com.fresnohernandez99.stpt.platform.PlatformUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.ExperimentalTime

class PlatformViewModel(
    private val platformInfo: Platform,
    private val platformUtils: PlatformUtils,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    private val _state = MutableStateFlow(PlatformUiState())
    val state: StateFlow<PlatformUiState> = _state

    init {
        loadAppInfo()
    }

    private fun loadAppInfo() {
        _state.value = _state.value.copy(
            appVersion = platformInfo.appVersion,
            platformName = platformInfo.name,
            isAndroid = platformInfo.isAndroid,
            isTablet = platformInfo.isTablet,
            isLandscape = platformInfo.isLandscape
        )
    }

    fun shareText(text: String) {
        if (text.isNotBlank()) {
            platformUtils.shareText(text)
        }
    }

    fun shareRecording(path: String) {
        if (path.isNotBlank()) {
            if (_state.value.isAndroid) {
                platformUtils.shareRecording(path)
            } else {
                onExportAudio(path)
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    fun onExportAudio(path: String) {
        if (path.isNotBlank()) {
            val defaultFileName =
                "recording_${kotlin.time.Clock.System.now().toEpochMilliseconds()}.wav"
            _state.value = _state.value.copy(isExporting = true)

            platformUtils.exportRecordingWithFilePicker(
                sourcePath = path,
                fileName = defaultFileName
            ) { success, message ->
                _state.value = _state.value.copy(
                    isExporting = false,
                    exportSuccess = success,
                    exportMessage = message
                        ?: if (success) "Audio exported successfully" else "Failed to export audio"
                )
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    fun onExportTextAsTxt(text: String) {
        if (text.isNotBlank()) {
            val defaultFileName = "text_${kotlin.time.Clock.System.now().toEpochMilliseconds()}.txt"
            _state.value = _state.value.copy(isExporting = true)

            platformUtils.exportTextWithFilePicker(
                text = text,
                fileName = defaultFileName
            ) { success, message ->
                _state.value = _state.value.copy(
                    isExporting = false,
                    exportSuccess = success,
                    exportMessage = message
                        ?: if (success) "Text exported successfully" else "Failed to export text"
                )
            }
        }
    }

    fun onCopy(text: String) {
        if (text.isNotBlank()) {
            onClearCopyState()
            platformUtils.copyTextToClipboard(text) { success, _ ->
                _state.value = _state.value.copy(
                    copySuccess = success
                )
            }
        }
    }

    fun onClearCopyState() {
        _state.value = _state.value.copy(copySuccess = null)
    }

    fun clearExportStatus() {
        _state.value = _state.value.copy(
            exportSuccess = null,
            exportMessage = null
        )
    }
}

data class PlatformUiState(
    val appVersion: String = "",
    val platformName: String = "",
    val isAndroid: Boolean = false,
    val isTablet: Boolean = false,
    val isLandscape: Boolean = false,
    val isExporting: Boolean = false,
    val exportSuccess: Boolean? = null,
    val exportMessage: String? = null,
    val copySuccess: Boolean? = null
)
