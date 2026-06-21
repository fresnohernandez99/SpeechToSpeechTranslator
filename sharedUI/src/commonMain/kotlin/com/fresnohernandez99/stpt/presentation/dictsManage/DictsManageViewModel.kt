package com.fresnohernandez99.stpt.presentation.dictsManage

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fresnohernandez99.stpt.domain.model.Language
import com.fresnohernandez99.stpt.domain.repository.DictRepository
import com.fresnohernandez99.stpt.platform.DownloadStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DictsManageState(
    val downloadedLanguages: List<Language> = emptyList(),
    val sourceLanguage: Language = Language.list.first(),
    val targetLanguage: Language = Language.Spanish,
    val isDownloading: Boolean = false,
    val downloadProgress: String = "",
    val errorMessage: String? = null
)

@Immutable
class DictsManageViewModel(
    private val dictRepository: DictRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DictsManageState())
    val state = _state.asStateFlow()

    init {
        loadDownloadedLanguages()
    }

    fun onSourceLanguageChange(language: Language) {
        _state.update { it.copy(sourceLanguage = language) }
    }

    fun onTargetLanguageChange(language: Language) {
        _state.update { it.copy(targetLanguage = language) }
    }

    private fun loadDownloadedLanguages() {
        viewModelScope.launch {
            val languages = dictRepository.getDownloadedLanguages()
            _state.update { it.copy(downloadedLanguages = languages) }
        }
    }

    fun onAddDictionary() {
        val source = _state.value.sourceLanguage
        val target = _state.value.targetLanguage

        viewModelScope.launch {
            downloadIfNeeded(source.code)
            downloadIfNeeded(target.code)
            loadDownloadedLanguages()
        }
    }

    private suspend fun downloadIfNeeded(code: String) {
        if (!dictRepository.isLanguageDownloaded(code)) {
            dictRepository.downloadLanguage(code).collect { status ->
                when (status) {
                    is DownloadStatus.Downloading -> {
                        _state.update {
                            it.copy(
                                isDownloading = true,
                                downloadProgress = "Downloading $code..."
                            )
                        }
                    }

                    is DownloadStatus.Success -> {
                        _state.update { it.copy(isDownloading = false, downloadProgress = "") }
                    }

                    is DownloadStatus.Error -> {
                        _state.update {
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

    fun onDeleteLanguage(code: String) {
        viewModelScope.launch {
            if (dictRepository.deleteLanguage(code)) {
                loadDownloadedLanguages()
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
}
