package com.fresnohernandez99.stpt.presentation.settings

import com.fresnohernandez99.stpt.domain.model.Language
import com.fresnohernandez99.stpt.fakes.FakePreferencesRepository
import com.fresnohernandez99.stpt.modelDownloader.FARSI
import com.fresnohernandez99.stpt.modelDownloader.OPTIMIZED_MODEL_SELECTION
import com.fresnohernandez99.stpt.presentation.BaseViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest : BaseViewModelTest() {

    private val fakePreferencesRepository = FakePreferencesRepository()
    private lateinit var viewModel: SettingsViewModel

    @Test
    fun onLanguageSelected_updatesRepository() = runTest {
        viewModel = SettingsViewModel(fakePreferencesRepository)
        val spanish = Language("es", "Spanish")
        
        viewModel.onLanguageSelected(spanish)
        advanceUntilIdle()
        
        assertEquals("es", fakePreferencesRepository.getDefaultTranscriptionLanguage().first())
    }

    @Test
    fun onLanguageSelected_farsi_updatesModelSelection() = runTest {
        viewModel = SettingsViewModel(fakePreferencesRepository)
        val farsi = Language(FARSI, "Persian")
        
        viewModel.onLanguageSelected(farsi)
        advanceUntilIdle()
        
        assertEquals(OPTIMIZED_MODEL_SELECTION, fakePreferencesRepository.getModelSelection().first())
    }
}
