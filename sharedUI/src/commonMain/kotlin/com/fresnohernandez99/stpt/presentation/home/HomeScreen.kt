package com.fresnohernandez99.stpt.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.fresnohernandez99.stpt.presentation.components.AppScaffold
import com.fresnohernandez99.stpt.presentation.home.components.HomeContent
import com.fresnohernandez99.stpt.presentation.home.components.RecordingDialog
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
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            BottomAppBar {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    FloatingActionButton(
                        onClick = { viewModel.onRecordingStateChange(true) },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Record Audio"
                        )
                    }
                }
            }
        }
    ) { padding ->
        when (windowSize) {
            WindowSize.Compact -> {
                HomeContent(
                    uiState = uiState,
                    onTextChanged = viewModel::onTextChanged,
                    onSourceLanguageSelected = viewModel::onSourceLanguageSelected,
                    onTargetLanguageSelected = viewModel::onTargetLanguageSelected,
                    onTranslateClick = viewModel::translate,
                    modifier = Modifier.padding(padding)
                )
            }

            WindowSize.Medium, WindowSize.Expanded -> {
                HomeContent(
                    uiState = uiState,
                    onTextChanged = viewModel::onTextChanged,
                    onSourceLanguageSelected = viewModel::onSourceLanguageSelected,
                    onTargetLanguageSelected = viewModel::onTargetLanguageSelected,
                    onTranslateClick = viewModel::translate,
                    modifier = Modifier.padding(padding)
                )
            }
        }

        if (uiState.isRecording)
            RecordingDialog(
                onDismiss = { viewModel.onRecordingStateChange(false) },
                onUpdateRecordingPath = viewModel::onUpdateRecordingPath
            )
    }
}
