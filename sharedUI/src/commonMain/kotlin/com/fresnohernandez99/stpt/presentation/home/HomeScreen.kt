package com.fresnohernandez99.stpt.presentation.home

import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.fresnohernandez99.stpt.presentation.components.AppScaffold
import com.fresnohernandez99.stpt.presentation.home.components.HomeContent
import com.fresnohernandez99.stpt.presentation.navigation.Destination
import com.fresnohernandez99.stpt.theme.WindowSize
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    link: Destination.Home,
    windowSize: WindowSize,
    navHostController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()

    AppScaffold(
        modifier = Modifier.imePadding(),
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        when (windowSize) {
            WindowSize.Compact -> {
                HomeContent(
                    uiState = uiState,
                    onTextChanged = viewModel::onTextChanged,
                    onSourceLanguageSelected = viewModel::onSourceLanguageSelected,
                    onTargetLanguageSelected = viewModel::onTargetLanguageSelected,
                    onTranslateClick = viewModel::translate,
                    modifier = Modifier
                )
            }

            WindowSize.Medium, WindowSize.Expanded -> {
                HomeContent(
                    uiState = uiState,
                    onTextChanged = viewModel::onTextChanged,
                    onSourceLanguageSelected = viewModel::onSourceLanguageSelected,
                    onTargetLanguageSelected = viewModel::onTargetLanguageSelected,
                    onTranslateClick = viewModel::translate,
                    modifier = Modifier
                )
            }
        }
    }
}
