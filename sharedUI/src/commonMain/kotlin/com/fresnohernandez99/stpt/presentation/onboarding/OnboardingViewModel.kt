package com.fresnohernandez99.stpt.presentation.onboarding

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fresnohernandez99.stpt.domain.repository.PreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

@Immutable
class OnboardingViewModel(
    private val preferencesRepository: PreferencesRepository
): ViewModel() {

    fun completeOnBoarding(onDone: ()-> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            preferencesRepository.setOnboardingCompleted(true)
            viewModelScope.launch(Dispatchers.Main) {
                onDone()
            }
        }
    }
}