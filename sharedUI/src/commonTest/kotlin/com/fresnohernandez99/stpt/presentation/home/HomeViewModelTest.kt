package com.fresnohernandez99.stpt.presentation.home

import com.fresnohernandez99.stpt.fakes.FakeDictRepository
import com.fresnohernandez99.stpt.fakes.FakePreferencesRepository
import com.fresnohernandez99.stpt.fakes.FakeTranslationHistoryRepository
import com.fresnohernandez99.stpt.fakes.FakeAudioRecorderPlayer
import com.fresnohernandez99.stpt.presentation.BaseViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest : BaseViewModelTest() {

    private val fakeDictRepository = FakeDictRepository()
    private val fakeHistoryRepository = FakeTranslationHistoryRepository()
    private val fakePreferencesRepository = FakePreferencesRepository()
    private val fakeAudioPlayer = FakeAudioRecorderPlayer()
    private lateinit var viewModel: HomeViewModel

    @Test
    fun onTextChanged_updatesUiState() = runTest {
        viewModel = HomeViewModel(fakeDictRepository, fakeHistoryRepository, fakePreferencesRepository, fakeAudioPlayer)
        
        viewModel.onTextChanged("Hello")
        
        assertEquals("Hello", viewModel.uiState.value.textToTranslate)
        assertEquals(TranslateState.NOT_REQUESTED, viewModel.uiState.value.translateState)
    }

    @Test
    fun translate_success_updatesStateAndHistory() = runTest {
        viewModel = HomeViewModel(fakeDictRepository, fakeHistoryRepository, fakePreferencesRepository, fakeAudioPlayer)
        viewModel.onTextChanged("Hello")
        fakeDictRepository.translateResult = "Hola"
        
        // Ensure initial prefs are set
        fakePreferencesRepository.setLanguagePref(
            com.fresnohernandez99.stpt.domain.model.LanguagesInPref(
                com.fresnohernandez99.stpt.domain.model.Language.English, 
                com.fresnohernandez99.stpt.domain.model.Language.Spanish
            )
        )

        viewModel.translate()
        advanceUntilIdle()
        
        assertEquals("Hola", viewModel.uiState.value.translatedText)
        assertEquals(TranslateState.SUCCESS, viewModel.uiState.value.translateState)
        assertEquals(1, fakeHistoryRepository.items.size)
        assertEquals("Hola", fakeHistoryRepository.items.first().translatedText)
    }

    @Test
    fun translate_error_updatesErrorMessage() = runTest {
        viewModel = HomeViewModel(fakeDictRepository, fakeHistoryRepository, fakePreferencesRepository, fakeAudioPlayer)
        viewModel.onTextChanged("Hello")
        fakeDictRepository.shouldThrow = true
        
        // Ensure initial prefs are set
        fakePreferencesRepository.setLanguagePref(
            com.fresnohernandez99.stpt.domain.model.LanguagesInPref(
                com.fresnohernandez99.stpt.domain.model.Language.English, 
                com.fresnohernandez99.stpt.domain.model.Language.Spanish
            )
        )

        viewModel.translate()
        advanceUntilIdle()
        
        assertEquals(TranslateState.ERROR, viewModel.uiState.value.translateState)
        assertEquals("Translation error", viewModel.uiState.value.errorMessage)
    }
}

