package com.fresnohernandez99.stpt.presentation.history

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fresnohernandez99.stpt.data.local.TranslatedItem
import com.fresnohernandez99.stpt.domain.repository.TranslationHistoryRepository
import com.fresnohernandez99.stpt.domain.repository.WordDefinitionRepository
import com.fresnohernandez99.stpt.utils.isSingleWord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Immutable
class HistoryViewModel(
    private val translationHistoryRepository: TranslationHistoryRepository,
    private val wordDefinitionRepository: WordDefinitionRepository
) : ViewModel() {

    val history = translationHistoryRepository.getTranslationHistory()

    private val _selectedItem = MutableStateFlow<TranslatedItem?>(null)
    val selectedItem = _selectedItem.asStateFlow()

    fun deleteTranslation(item: TranslatedItem) {
        viewModelScope.launch(Dispatchers.IO) {
            translationHistoryRepository.deleteTranslation(item)
        }
    }

    fun loadWordDefinition(item: TranslatedItem) {
        viewModelScope.launch(Dispatchers.IO) {
            if (item.dictionaryResponse != null) return@launch

            val response =
                wordDefinitionRepository.getWordDefinition(item.translatedTo, item.translatedText)

            if (response.isSuccess) {
                translationHistoryRepository.updateTranslation(
                    item.copy(
                        dictionaryResponse = response.getOrNull()
                    )
                )
            }
        }
    }

    fun selectItem(item: TranslatedItem) {
        _selectedItem.value = item

        if (item.translatedText.isSingleWord())
            loadWordDefinition(item)
    }
}
