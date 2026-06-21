package com.fresnohernandez99.stpt.modelDownloader

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fresnohernandez99.stpt.platform.Downloader
import com.fresnohernandez99.stpt.platform.Transcriber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Immutable
class ModelDownloaderViewModel(
    private val downloader: Downloader,
    private val transcriber: Transcriber,
    private val modelSelection: ModelSelection
) : ViewModel() {

    private var _uiState: MutableStateFlow<DownloaderUiState> = MutableStateFlow(
        DownloaderUiState(
            modelSelection.getDefaultTranscriptionModel()
        )
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val selectedModel = modelSelection.getSelectedModel()
            _uiState.value = DownloaderUiState(selectedModel)
        }
    }

    val uiState: StateFlow<DownloaderUiState> = _uiState

    private val _effects = MutableSharedFlow<Pair<DownloaderEffect, String?>>()
    val effects: SharedFlow<Pair<DownloaderEffect, String?>> = _effects

    var currentFilePath = ""

    fun checkTranscriptionAvailability(filePath: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            _effects.emit(DownloaderEffect.CheckingEffect() to filePath)
            if (filePath != null) currentFilePath = filePath

            if (downloader.hasRunningDownload()) {
                trackDownload()
            } else {
                if (!transcriber.doesModelExists(uiState.value.selectedModel.name)
                    || !transcriber.isValidModel(uiState.value.selectedModel.name)
                ) {
                    _effects.emit(DownloaderEffect.AskForUserAcceptance() to filePath)
                } else {
                    _effects.emit(DownloaderEffect.ModelsAreReady() to filePath)
                }
            }
        }
    }

    fun startDownload() {
        viewModelScope.launch(Dispatchers.IO) {
            val modelUrl = uiState.value.selectedModel.url
//            if (modelUrl != null) {
            downloader.startDownload(modelUrl, uiState.value.selectedModel.name)
            trackDownload()
//            } else {
//                _effects.emit(DownloaderEffect.ErrorEffect())
//            }
        }
    }

    private suspend fun trackDownload() {
        _effects.emit(DownloaderEffect.DownloadEffect() to currentFilePath)
        downloader.trackDownloadProgress(
            uiState.value.selectedModel.name,
            onProgressUpdated = { progress, downloadedMB, totalMB ->
                _uiState.update { current ->
                    current.copy(
                        progress = progress.toFloat(),
                        downloaded = downloadedMB,
                        total = totalMB
                    )
                }
            }, onSuccess = {
                viewModelScope.launch {
                    transcriber.initialize(uiState.value.selectedModel.name)
                    _effects.emit(DownloaderEffect.ModelsAreReady() to currentFilePath)
                }

            }, onFailed = {
                viewModelScope.launch { _effects.emit(DownloaderEffect.ErrorEffect() to currentFilePath) }
            })
    }
}
