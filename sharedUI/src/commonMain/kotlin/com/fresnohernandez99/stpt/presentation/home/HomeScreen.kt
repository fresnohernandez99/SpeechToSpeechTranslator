package com.fresnohernandez99.stpt.presentation.home

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fresnohernandez99.stpt.domain.model.Language
import com.fresnohernandez99.stpt.modelDownloader.DownloadModelDialog
import com.fresnohernandez99.stpt.modelDownloader.DownloaderEffect
import com.fresnohernandez99.stpt.modelDownloader.ModelDownloaderViewModel
import com.fresnohernandez99.stpt.modelDownloader.components.DownloaderDialog
import com.fresnohernandez99.stpt.presentation.components.AppScaffold
import com.fresnohernandez99.stpt.presentation.components.PreparingLoadingDialog
import com.fresnohernandez99.stpt.presentation.home.components.HomeContent
import com.fresnohernandez99.stpt.presentation.home.components.LanguageSelector
import com.fresnohernandez99.stpt.presentation.home.components.RecordingDialog
import com.fresnohernandez99.stpt.presentation.navigation.Destination
import com.fresnohernandez99.stpt.presentation.navigation.LocalNavController
import com.fresnohernandez99.stpt.theme.LocalWindowSizeHelper
import com.fresnohernandez99.stpt.theme.WindowSize
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import speechtospeechtranslator.sharedui.generated.resources.Res
import speechtospeechtranslator.sharedui.generated.resources.confirmation_cancel
import speechtospeechtranslator.sharedui.generated.resources.download_dialog_error
import speechtospeechtranslator.sharedui.generated.resources.settings

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationStyleApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    downloaderViewModel: ModelDownloaderViewModel = koinViewModel(),
    link: Destination.Home
) {
    val navHostController = LocalNavController.current
    val windowSize = LocalWindowSizeHelper.current

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

    var showLoadingDialog by remember { mutableStateOf(false) }
    var showDownloadDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var showDownloadQuestionDialog by remember { mutableStateOf(false) }

    val downloaderUiState by downloaderViewModel.uiState.collectAsStateWithLifecycle()

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
            Column(
                Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceContainer).padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LanguageSelector(
                        selectedLanguage = uiState.sourceLanguage,
                        languages = Language.list,
                        onLanguageSelected = viewModel::onSourceLanguageSelected,
                        modifier = Modifier.weight(1f)
                    )

                    Text("→", fontWeight = FontWeight.Bold)

                    LanguageSelector(
                        selectedLanguage = uiState.targetLanguage,
                        languages = Language.getFilteredLanguages(
                            allLanguages = Language.list,
                            priorityLanguage = Language.Spanish,
                            excludeLanguages = listOf(Language.Detect)
                        ),
                        onLanguageSelected = viewModel::onTargetLanguageSelected,
                        modifier = Modifier.weight(1f)
                    )
                }

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

        if (uiState.isDownloading) {
            Dialog(onDismissRequest = {}) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(uiState.downloadProgress)
                    }
                }
            }
        }

        uiState.errorMessage?.let { error ->
            AlertDialog(
                onDismissRequest = viewModel::clearError,
                title = { Text("Error") },
                text = { Text(error) },
                confirmButton = {
                    TextButton(onClick = viewModel::clearError) {
                        Text("OK")
                    }
                }
            )
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
                onSetPlaybackSpeed = viewModel::setPlaybackSpeed,
                onCompletedRecord = {
                    viewModel.onCompletedRecording {
                        downloaderViewModel.checkTranscriptionAvailability(it)
                    }
                }
            )

        if (showLoadingDialog) {
            PreparingLoadingDialog()
        }

        if (showDownloadDialog) {
            LocalSoftwareKeyboardController.current?.hide()
            DownloaderDialog(
                transcriptionModel = downloaderUiState.selectedModel,
                downloaderUiState,
                onDismiss = { showDownloadDialog = false }
            )
        }

        if (showErrorDialog) {
            LocalSoftwareKeyboardController.current?.hide()
            AlertDialog(
                modifier = Modifier.height(120.dp),
                onDismissRequest = { showErrorDialog = false },
                title = {
                    Text(stringResource(resource = Res.string.download_dialog_error))
                },
                confirmButton = {
                    Button(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 24.dp),
                        onClick = {
                            showErrorDialog = false
                        },
                    ) {
                        Text(
                            stringResource(resource = Res.string.confirmation_cancel),
                            color = Color.White
                        )
                    }
                }
            )

        }

        if (showDownloadQuestionDialog) {
            LocalSoftwareKeyboardController.current?.hide()
            DownloadModelDialog(
                onDownload = {
                    downloaderViewModel.startDownload()
                    showDownloadQuestionDialog = false
                },
                onCancel = {
                    showDownloadQuestionDialog = false
                },
                transcriptionModel = downloaderUiState.selectedModel
            )
        }

        LaunchedEffect(Unit) {
            viewModel.onTextChanged(link.typed)

            downloaderViewModel.effects.collect {
                when (it.first) {
                    is DownloaderEffect.DownloadEffect -> {
                        showDownloadDialog = true
                        showDownloadQuestionDialog = false
                        showLoadingDialog = false
                    }

                    is DownloaderEffect.ErrorEffect -> {
                        showDownloadDialog = false
                        showErrorDialog = true
                        showLoadingDialog = false
                    }

                    is DownloaderEffect.ModelsAreReady -> {
                        showDownloadDialog = false
                        showLoadingDialog = false
                        if (it.second != null)
                            navHostController.navigate(
                                Destination.Transcription(
                                    audioPath = it.second!!
                                )
                            )
                    }

                    is DownloaderEffect.AskForUserAcceptance -> {
                        showDownloadQuestionDialog = true
                        showLoadingDialog = false
                    }

                    is DownloaderEffect.CheckingEffect -> {
                        showLoadingDialog = true
                        showDownloadDialog = false
                    }
                }
            }
        }
    }
}
