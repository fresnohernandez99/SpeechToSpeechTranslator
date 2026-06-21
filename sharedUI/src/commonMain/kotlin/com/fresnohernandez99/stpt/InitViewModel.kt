package com.fresnohernandez99.stpt

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fresnohernandez99.stpt.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Immutable
sealed interface InitUiState {
    data object Loading : InitUiState
    data object Resolved : InitUiState
    data object Unfinished : InitUiState
}

@Immutable
class InitViewModel(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<InitUiState>(InitUiState.Loading)
    val uiState: StateFlow<InitUiState> = _uiState.asStateFlow()

    init {
        checkStatus()
    }

    private fun checkStatus() {
        viewModelScope.launch {
            val hasCompletedOnboarding = preferencesRepository.hasCompletedOnboarding()
            
            if (hasCompletedOnboarding) {
                _uiState.value = InitUiState.Resolved
            } else {
                _uiState.value = InitUiState.Unfinished
            }
        }
    }
}
