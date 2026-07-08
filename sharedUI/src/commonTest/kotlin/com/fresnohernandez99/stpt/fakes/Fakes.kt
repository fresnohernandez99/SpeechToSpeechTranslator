package com.fresnohernandez99.stpt.fakes

import androidx.paging.PagingData
import com.fresnohernandez99.stpt.data.local.TranslatedItem
import com.fresnohernandez99.stpt.data.remote.model.DictionaryResponse
import com.fresnohernandez99.stpt.domain.model.Language
import com.fresnohernandez99.stpt.domain.model.LanguagesInPref
import com.fresnohernandez99.stpt.domain.repository.DictRepository
import com.fresnohernandez99.stpt.domain.repository.PreferencesRepository
import com.fresnohernandez99.stpt.domain.repository.TranslationHistoryRepository
import com.fresnohernandez99.stpt.domain.repository.WordDefinitionRepository
import com.fresnohernandez99.stpt.platform.DownloadStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf

class FakePreferencesRepository : PreferencesRepository {
    private val languagePref = MutableStateFlow(LanguagesInPref(Language.English, Language.Spanish))
    private val defaultTranscription = MutableStateFlow("en")
    private val modelSelection = MutableStateFlow(0)
    private val downloadId = MutableStateFlow(0L)
    private var onboardingCompleted = false

    override suspend fun hasCompletedOnboarding(): Boolean = onboardingCompleted
    override suspend fun setOnboardingCompleted(completed: Boolean) { onboardingCompleted = completed }

    override fun getLanguagePref(): Flow<LanguagesInPref> = languagePref
    override suspend fun setLanguagePref(pref: LanguagesInPref) {
        languagePref.value = pref
    }

    override fun getDefaultTranscriptionLanguage(): Flow<String> = defaultTranscription
    override suspend fun setDefaultTranscriptionLanguage(language: String) {
        defaultTranscription.value = language
    }

    override fun getModelSelection(): Flow<Int> = modelSelection
    override suspend fun setModelSelection(modelSelection: Int) {
        this.modelSelection.value = modelSelection
    }

    override fun getModelDownloadId(): Flow<Long> = downloadId
    override suspend fun setModelDownloadId(downloadId: Long) {
        this.downloadId.value = downloadId
    }
}

class FakeDictRepository : DictRepository {
    var translateResult = "Translated Text"
    var detectedLanguage = Language.English
    var shouldThrow = false

    override suspend fun getDownloadedLanguages(): List<Language> = emptyList()
    override suspend fun isLanguageDownloaded(code: String): Boolean = true
    override fun downloadLanguage(code: String): Flow<DownloadStatus> = flowOf(DownloadStatus.Success)
    override suspend fun deleteLanguage(code: String): Boolean = true
    override suspend fun translate(text: String, source: String, target: String): String {
        if (shouldThrow) throw Exception("Translation error")
        return translateResult
    }
    override suspend fun getLanguage(text: String): Language = detectedLanguage
}

class FakeTranslationHistoryRepository : TranslationHistoryRepository {
    val items = mutableListOf<TranslatedItem>()

    override suspend fun addTranslation(item: TranslatedItem) {
        items.add(item)
    }

    override suspend fun getLast3(): List<TranslatedItem> = items.takeLast(3).reversed()
    override fun getTranslationHistory(): Flow<PagingData<TranslatedItem>> = flowOf(PagingData.from(items))
    override suspend fun deleteTranslation(item: TranslatedItem) {
        items.remove(item)
    }
    override suspend fun updateTranslation(item: TranslatedItem) {
        val index = items.indexOfFirst { it.id == item.id }
        if (index != -1) items[index] = item
    }
}

class FakeWordDefinitionRepository : WordDefinitionRepository {
    var result: Result<DictionaryResponse> = Result.failure(Exception("Not implemented"))

    override suspend fun getWordDefinition(languageCode: String, word: String): Result<DictionaryResponse> = result
}
