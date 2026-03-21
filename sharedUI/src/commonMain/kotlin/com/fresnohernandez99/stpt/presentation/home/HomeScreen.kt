package com.fresnohernandez99.stpt.presentation.home

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.navigation.NavHostController
import com.fresnohernandez99.stpt.presentation.components.AppScaffold
import com.fresnohernandez99.stpt.presentation.home.components.HomeContent
import com.fresnohernandez99.stpt.presentation.home.components.RecordingDialog
import com.fresnohernandez99.stpt.presentation.navigation.Destination
import com.fresnohernandez99.stpt.theme.WindowSize
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import speechtospeechtranslator.sharedui.generated.resources.Res
import speechtospeechtranslator.sharedui.generated.resources.settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    link: Destination.Home,
    windowSize: WindowSize,
    navHostController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()

    val (showRecord, setShowRecord) = remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "recording")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val fabScale = if (uiState.isRecording) scale else 1f

    AppScaffold(
        modifier = Modifier.imePadding(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = { navHostController.navigate(Destination.Settings) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(Res.string.settings),
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            BottomAppBar {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    FloatingActionButton(
                        onClick = {
                            setShowRecord(true)
                            viewModel.onRecordingStateChange(!uiState.isRecording)
                        },
                        containerColor = if (uiState.isRecording) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                        contentColor = if (uiState.isRecording) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.graphicsLayer {
                            scaleX = fabScale
                            scaleY = fabScale
                        }
                    ) {
                        Icon(
                            imageVector = if (uiState.isRecording) Icons.Default.Stop else Icons.Default.Mic,
                            contentDescription = if (uiState.isRecording) "Stop Recording" else "Record Audio"
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

        if (showRecord)
            RecordingDialog(
                onDismiss = {
                    viewModel.stopRecording()
                    setShowRecord(false)
                },
                uiState = uiState,
                onStartRecording = viewModel::startRecording,
                onStopRecording = viewModel::stopRecording,
                onPauseRecording = viewModel::pauseRecording,
                onResumeRecording = viewModel::resumeRecording,
                onStartPlaying = viewModel::startPlaying,
                onStopPlaying = viewModel::stopPlaying,
                onPausePlaying = viewModel::pausePlaying,
                onResumePlaying = viewModel::resumePlaying,
                onSetPlaybackSpeed = viewModel::setPlaybackSpeed
            )
    }
}
