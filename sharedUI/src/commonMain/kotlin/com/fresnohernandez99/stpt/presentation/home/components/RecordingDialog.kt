package com.fresnohernandez99.stpt.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.fresnohernandez99.stpt.presentation.home.HomeUiState
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RecordingDialog(
    onDismiss: () -> Unit,
    uiState: HomeUiState,
    onStartRecording: () -> Unit,
    onStopRecording: () -> Unit,
    onPauseRecording: () -> Unit,
    onResumeRecording: () -> Unit,
    onStartPlaying: () -> Unit,
    onStopPlaying: () -> Unit,
    onPausePlaying: () -> Unit,
    onResumePlaying: () -> Unit,
    onSetPlaybackSpeed: (Float) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF455A64))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "Audio Recorder Player!!",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(top = 84.dp)
            )

            // Error message
            uiState.errorMessage?.let { error ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.8f))
                ) {
                    Text(
                        text = error,
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Record time display
            Text(
                text = uiState.recordTime,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.W200,
                letterSpacing = 3.sp,
                modifier = Modifier.padding(top = 32.dp)
            )

            // Audio metering display (only show when recording)
            if (uiState.isRecording) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Recording Level",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Metering bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .background(Color(0xFF263238), RoundedCornerShape(4.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(uiState.meteringLevel)
                                .background(
                                    when {
                                        uiState.meteringLevel > 0.8f -> Color.Red
                                        uiState.meteringLevel > 0.6f -> Color.Yellow
                                        else -> Color.Green
                                    },
                                    RoundedCornerShape(4.dp)
                                )
                        )
                    }

                    Text(
                        text = "${(uiState.meteringLevel * 100).roundToInt()}%",
                        color = Color(0xFFCCCCCC),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Recording info display
            uiState.recordingInfo?.let { info ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF37474F))
                ) {
                    Text(
                        text = info,
                        color = Color.White,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // Recording controls
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        12.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AudioButton(
                        text = "Record",
                        onClick = onStartRecording,
                        enabled = !uiState.isRecording && !uiState.isPlaying
                    )
                    AudioButton(
                        text = "Pause",
                        onClick = onPauseRecording,
                        enabled = uiState.isRecording && !uiState.isRecordingPaused
                    )
                    AudioButton(
                        text = "Resume",
                        onClick = onResumeRecording,
                        enabled = uiState.isRecording && uiState.isRecordingPaused
                    )
                    AudioButton(
                        text = "Stop",
                        onClick = onStopRecording,
                        enabled = uiState.isRecording
                    )
                }
            }

            // Player section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Progress bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 28.dp)
                        .height(4.dp)
                        .background(Color(0xFFCCCCCC))
                        .clickable {
                            // TODO: Calculate position based on click and seek
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(uiState.playProgress)
                            .background(Color.White)
                    )
                }

                // Time display
                Text(
                    text = "${uiState.playTime} / ${uiState.duration}",
                    color = Color(0xFFCCCCCC),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )

                // Playback speed control
                if (uiState.isPlaying) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 28.dp)
                            .padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Playback Speed: ${uiState.playbackSpeed}x",
                            color = Color.White,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Slider(
                            value = uiState.playbackSpeed,
                            onValueChange = onSetPlaybackSpeed,
                            valueRange = 0.5f..2.0f,
                            steps = 5,
                            modifier = Modifier.fillMaxWidth(),
                            colors = SliderDefaults.colors(
                                thumbColor = Color.White,
                                activeTrackColor = Color.White,
                                inactiveTrackColor = Color(0xFF37474F)
                            )
                        )

                        // Speed preset buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            listOf(0.5f, 0.75f, 1.0f, 1.5f, 2.0f).forEach { speed ->
                                TextButton(
                                    onClick = { onSetPlaybackSpeed(speed) },
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = if (uiState.playbackSpeed == speed) Color.White else Color(
                                            0xFFCCCCCC
                                        )
                                    )
                                ) {
                                    Text(
                                        text = "${speed}x",
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Player controls
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        12.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AudioButton(
                        text = "Play",
                        onClick = onStartPlaying,
                        enabled = !uiState.isRecording && !uiState.isPlaying
                    )
                    AudioButton(
                        text = "Pause",
                        onClick = onPausePlaying,
                        enabled = uiState.isPlaying && !uiState.isPlayingPaused
                    )
                    AudioButton(
                        text = "Resume",
                        onClick = onResumePlaying,
                        enabled = uiState.isPlaying && uiState.isPlayingPaused
                    )
                    AudioButton(
                        text = "Stop",
                        onClick = onStopPlaying,
                        enabled = uiState.isPlaying
                    )
                }
            }
        }
    }
}

@Composable
fun AudioButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .defaultMinSize(minWidth = 88.dp, minHeight = 48.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Color.White,
            disabledContentColor = Color.Gray
        )
    ) {
        Text(
            text = text,
            color = if (enabled) Color.White else Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}
