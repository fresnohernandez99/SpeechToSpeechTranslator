package com.fresnohernandez99.stpt.transcription

import MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fresnohernandez99.stpt.platform.HandlePlatformBackNavigation
import com.fresnohernandez99.stpt.presentation.components.AppScaffold
import com.fresnohernandez99.stpt.presentation.navigation.Destination
import com.fresnohernandez99.stpt.presentation.navigation.LocalNavController
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import speechtospeechtranslator.sharedui.generated.resources.Res
import speechtospeechtranslator.sharedui.generated.resources.top_bar_back
import speechtospeechtranslator.sharedui.generated.resources.transcription_dialog_append
import speechtospeechtranslator.sharedui.generated.resources.transcription_dialog_error_audio_file_desc
import speechtospeechtranslator.sharedui.generated.resources.transcription_dialog_error_audio_file_title
import speechtospeechtranslator.sharedui.generated.resources.transcription_dialog_error_got_it

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranscriptionScreen(
    link: Destination.Transcription,
    viewModel: TranscriptionViewModel = koinViewModel()
) {
    val navHostController = LocalNavController.current

    val scrollState = rememberScrollState()
    val transcriptionUiState by viewModel.uiState.collectAsState()

    LaunchedEffect(transcriptionUiState.originalText) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    DisposableEffect(Unit) {
        viewModel.requestAudioPermission()
        viewModel.initRecognizer()
        viewModel.startRecognizer(link.audioPath)
        onDispose {
            viewModel.stopRecognizer()
            viewModel.finishRecognizer()
        }
    }

    AppScaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Transcripción",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            viewModel.stopRecognizer()
                            viewModel.finishRecognizer()
                            navHostController.navigateUp()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(Res.string.top_bar_back)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )

                // Progress indicator at the bottom of top bar
                if (transcriptionUiState.progress in 1..99) {
                    SmoothLinearProgressBar((transcriptionUiState.progress / 100f))
                } else if (transcriptionUiState.inTranscription && transcriptionUiState.progress == 0) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = !transcriptionUiState.inTranscription,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Button(
                        onClick = { navHostController.navigate(Destination.Home(typed = transcriptionUiState.originalText)) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            stringResource(Res.string.transcription_dialog_append),
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp),
                            color = Color.White
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            // Audio Player Card
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
            ) {
                MediaPlayer(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    url = link.audioPath,
                    startTime = MaterialTheme.colorScheme.onSurface,
                    endTime = MaterialTheme.colorScheme.onSurface,
                    volumeIconColor = MaterialTheme.colorScheme.primary,
                    playIconColor = MaterialTheme.colorScheme.primary,
                    sliderTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                    sliderIndicatorColor = MaterialTheme.colorScheme.primary,
                    showControls = true,
                    headers = mapOf(),
                    autoPlay = false
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Transcription Content Card
            OutlinedCard(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    if (transcriptionUiState.originalText.isEmpty() && transcriptionUiState.inTranscription) {
                        Text(
                            text = "Escuchando...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    } else {
                        Text(
                            text = if (transcriptionUiState.viewOriginalText) transcriptionUiState.originalText else transcriptionUiState.summarizedText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                lineHeight = 28.sp,
                                letterSpacing = 0.5.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    HandlePlatformBackNavigation(enabled = true) {
        navHostController.navigateUp()
    }

    if (transcriptionUiState.hasError) {
        AlertDialog(
            onDismissRequest = { navHostController.navigateUp() },
            confirmButton = {
                TextButton(onClick = { navHostController.navigateUp() }) {
                    Text(stringResource(Res.string.transcription_dialog_error_got_it))
                }
            },
            title = { Text(stringResource(Res.string.transcription_dialog_error_audio_file_title)) },
            text = { Text(stringResource(Res.string.transcription_dialog_error_audio_file_desc)) }
        )
    }
}

@Composable
fun SmoothLinearProgressBar(progress: Float) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500)
    )

    LinearProgressIndicator(
        progress = { animatedProgress },
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
        strokeCap = StrokeCap.Round,
    )
}
