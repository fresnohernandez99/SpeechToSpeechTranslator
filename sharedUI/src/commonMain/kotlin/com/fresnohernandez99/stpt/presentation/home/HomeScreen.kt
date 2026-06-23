package com.fresnohernandez99.stpt.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fresnohernandez99.stpt.modelDownloader.DownloadModelDialog
import com.fresnohernandez99.stpt.modelDownloader.DownloaderEffect
import com.fresnohernandez99.stpt.modelDownloader.ModelDownloaderViewModel
import com.fresnohernandez99.stpt.modelDownloader.components.DownloaderDialog
import com.fresnohernandez99.stpt.presentation.components.AppScaffold
import com.fresnohernandez99.stpt.presentation.components.PreparingLoadingDialog
import com.fresnohernandez99.stpt.presentation.home.components.HomeContent
import com.fresnohernandez99.stpt.presentation.home.components.LanguageSelectorTopBar
import com.fresnohernandez99.stpt.presentation.home.components.RecordingDialog
import com.fresnohernandez99.stpt.presentation.navigation.Destination
import com.fresnohernandez99.stpt.presentation.navigation.LocalNavController
import com.fresnohernandez99.stpt.theme.LocalWindowSizeHelper
import com.fresnohernandez99.stpt.theme.WindowSize
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.viewmodel.koinViewModel
import speechtospeechtranslator.sharedui.generated.resources.Res
import speechtospeechtranslator.sharedui.generated.resources.add_box
import speechtospeechtranslator.sharedui.generated.resources.camera
import speechtospeechtranslator.sharedui.generated.resources.confirmation_cancel
import speechtospeechtranslator.sharedui.generated.resources.download_dialog_error
import speechtospeechtranslator.sharedui.generated.resources.draw
import speechtospeechtranslator.sharedui.generated.resources.history
import speechtospeechtranslator.sharedui.generated.resources.menu
import speechtospeechtranslator.sharedui.generated.resources.mic

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

    var showLoadingDialog by remember { mutableStateOf(false) }
    var showDownloadDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var showDownloadQuestionDialog by remember { mutableStateOf(false) }

    val downloaderUiState by downloaderViewModel.uiState.collectAsStateWithLifecycle()

    var lastTranslated by remember { mutableStateOf("") }
    val enabledTranslationFunction by remember(lastTranslated, uiState.textToTranslate) {
        derivedStateOf {
            lastTranslated != uiState.textToTranslate && uiState.textToTranslate.isNotBlank()
        }
    }

    LaunchedEffect(uiState.translateState) {
        if (uiState.translateState == TranslateState.SUCCESS) {
            lastTranslated = uiState.textToTranslate
        }
    }

    AppScaffold(
        modifier = Modifier.imePadding(),
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            Box(Modifier.systemBarsPadding()) {
                LanguageSelectorTopBar(
                    modifier = Modifier,
                    sourceLanguage = uiState.sourceLanguage,
                    targetLanguage = uiState.targetLanguage,
                    onSourceLanguageSelected = viewModel::onSourceLanguageSelected,
                    onTargetLanguageSelected = viewModel::onTargetLanguageSelected
                )
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .navigationBarsPadding(), contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.elevatedCardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(
                            space = 16.dp,
                            alignment = Alignment.CenterHorizontally
                        )
                    )
                    {
                        IconButton(
                            enabled = false,
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.surface,
                                disabledContentColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3F),
                            ),
                            onClick = { /* do something */ }) {
                            Icon(
                                vectorResource(Res.drawable.camera),
                                modifier = Modifier.size(24.dp),
                                contentDescription = "Scan with camera icon",
                            )
                        }

                        IconButton(
                            enabled = false,
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.surface,
                                disabledContentColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3F),
                            ),
                            onClick = { /* do something */ }) {
                            Icon(
                                vectorResource(Res.drawable.add_box),
                                modifier = Modifier.size(24.dp),
                                contentDescription = "Add box icon",
                            )
                        }
                        IconButton(
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.surface,
                                disabledContentColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3F),
                            ),
                            onClick = {
                                setShowRecord(true)
                                viewModel.onRecordingStateChange(!uiState.isRecording)
                            }
                        ) {
                            Icon(
                                vectorResource(Res.drawable.mic),
                                modifier = Modifier.size(24.dp),
                                contentDescription = "Record button",
                            )
                        }
                        IconButton(
                            enabled = false,
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.surface,
                                disabledContentColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3F),
                            ),
                            onClick = { /* do something */ }) {
                            Icon(
                                vectorResource(Res.drawable.draw),
                                modifier = Modifier.size(24.dp),
                                contentDescription = "Draw text icon",
                            )
                        }
                        IconButton(
                            enabled = false,
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.surface,
                                disabledContentColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3F),
                            ),
                            onClick = { /* do something */ }) {
                            Icon(
                                vectorResource(Res.drawable.history),
                                modifier = Modifier.size(24.dp),
                                contentDescription = "Go history icon",
                            )
                        }
                        IconButton(
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.surface,
                                disabledContentColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3F),
                            ),
                            onClick = { navHostController.navigate(Destination.Settings) }) {
                            Icon(
                                vectorResource(Res.drawable.menu),
                                modifier = Modifier.size(24.dp),
                                contentDescription = "Go menu icon",
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        val topPadding = padding.calculateTopPadding()

        when (windowSize) {
            WindowSize.Compact -> {
                HomeContent(
                    uiState = uiState,
                    onTextChanged = viewModel::onTextChanged,
                    onTranslateClick = viewModel::translate,
                    modifier = Modifier
                        .padding(top = topPadding)
                        .consumeWindowInsets(padding),
                    enabledTranslationFunction = enabledTranslationFunction
                )
            }

            WindowSize.Medium, WindowSize.Expanded -> {
                HomeContent(
                    uiState = uiState,
                    onTextChanged = viewModel::onTextChanged,
                    onTranslateClick = viewModel::translate,
                    modifier = Modifier.padding(padding),
                    enabledTranslationFunction = enabledTranslationFunction
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
