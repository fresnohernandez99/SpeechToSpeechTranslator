package com.fresnohernandez99.stpt.transcription

import MediaPlayer
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fresnohernandez99.stpt.platform.HandlePlatformBackNavigation
import com.fresnohernandez99.stpt.presentation.navigation.Destination
import com.fresnohernandez99.stpt.theme.WindowSize
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import speechtospeechtranslator.sharedui.generated.resources.Res
import speechtospeechtranslator.sharedui.generated.resources.top_bar_back
import speechtospeechtranslator.sharedui.generated.resources.transcription_dialog_append
import speechtospeechtranslator.sharedui.generated.resources.transcription_dialog_error_audio_file_desc
import speechtospeechtranslator.sharedui.generated.resources.transcription_dialog_error_audio_file_title
import speechtospeechtranslator.sharedui.generated.resources.transcription_dialog_error_got_it

@Composable
fun TranscriptionScreen(
    link: Destination.Transcription,
    windowSize: WindowSize,
    navHostController: NavHostController,
    viewModel: TranscriptionViewModel = koinViewModel()
) {
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
    Card {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 48.dp)
        ) {
            Box(
                modifier = Modifier.align(Alignment.Start)
                    .padding(start = 4.dp, bottom = 12.dp, top = 4.dp)
            ) {
                BackButton(
                    onNavigateBack = {
                        viewModel.stopRecognizer()
                        viewModel.finishRecognizer()
                        navHostController.navigateUp()
                    }
                )
            }

            MediaPlayer(
                modifier = Modifier.fillMaxWidth(),
                url = link.audioPath,
                startTime = Color.Black,
                endTime = Color.Black,
                volumeIconColor = Color.Black,
                playIconColor = Color.Blue,
                sliderTrackColor = Color.LightGray,
                sliderIndicatorColor = Color.Blue,
                showControls = true,
                headers = mapOf(),
                autoPlay = false
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .border(
                        2.dp,
                        MaterialTheme.colorScheme.onBackground,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            ) {
                Text(
                    text = if (transcriptionUiState.viewOriginalText) transcriptionUiState.originalText else transcriptionUiState.summarizedText,
                )
            }
            if (transcriptionUiState.progress == 0) {
                LinearProgressIndicator(
                    modifier = Modifier.padding(vertical = 12.dp).fillMaxWidth(),
                    strokeCap = StrokeCap.Round
                )
            } else if (transcriptionUiState.progress in 1..99) {
                SmoothLinearProgressBar((transcriptionUiState.progress / 100f))
            }
//                FloatingActionButton(
//                    modifier = Modifier.padding(vertical = 8.dp),
//                    shape = CircleShape,
//                    onClick = {
//                        if (!transcriptionUiState.isListening) {
//                            onRecognitionStart()
//                        } else {
//                            onRecognitionStopped()
//                        }
//                    },
//                    backgroundColor = if (transcriptionUiState.isListening) Color.Red else Color.Green
//                ) {
//                    Icon(
//                        imageVector = Images.Icons.IcRecorder,
//                        contentDescription = stringResource(Res.string.note_detail_recorder),
//                        tint = LocalCustomColors.current.bodyContentColor
//                    )
//                }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    enabled = !transcriptionUiState.inTranscription,
                    border = BorderStroke(
                        width = 2.dp,
                        color = if (!transcriptionUiState.inTranscription) {
                            MaterialTheme.colorScheme.onBackground
                        } else {
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.38f)
                        }
                    ),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        disabledContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.38f)
                    ),
                    content = {
                        Text(
                            stringResource(Res.string.transcription_dialog_append)
                        )
                    },
                    onClick = {
//                        val result =
//                            if (transcriptionUiState.viewOriginalText) transcriptionUiState.originalText else transcriptionUiState.summarizedText
//                        editorViewModel.onUpdateContent(TextFieldValue("${editorState.content.text}\n$result"))
                        navHostController.navigateUp()
                    }
                )
            }

        }
    }

    HandlePlatformBackNavigation(enabled = true) {
        navHostController.navigateUp()
    }

    if (transcriptionUiState.hasError) {
        AlertDialog(
            onDismissRequest = {
                navHostController.navigateUp()
            },
            confirmButton = {
                TextButton(onClick = {
                    navHostController.navigateUp()
                }) {
                    Text(stringResource(Res.string.transcription_dialog_error_got_it))
                }
            },
            title = { Text(stringResource(Res.string.transcription_dialog_error_audio_file_title)) },
            text = { Text(stringResource(Res.string.transcription_dialog_error_audio_file_desc)) }
        )
    }

}

@Composable
fun BackButton(
    onNavigateBack: () -> Unit
) {
    IconButton(
        onClick = onNavigateBack,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(Res.string.top_bar_back),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}


@Composable
fun SmoothLinearProgressBar(progress: Float) {
    // Animate the progress value for smooth transitions
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500) // Adjust duration as needed
    )

    LinearProgressIndicator(
        progress = { animatedProgress },
        modifier = Modifier.padding(vertical = 12.dp).fillMaxWidth(),
        color = ProgressIndicatorDefaults.linearColor,
        trackColor = ProgressIndicatorDefaults.linearTrackColor,
        strokeCap = StrokeCap.Round,
    )
}





