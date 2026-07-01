package com.fresnohernandez99.stpt.presentation.home.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.styleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalMediaQueryApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.fresnohernandez99.stpt.presentation.home.HomeUiState
import kotlinx.coroutines.delay

@OptIn(
    ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMediaQueryApi::class
)
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
    onSetPlaybackSpeed: (Float) -> Unit,
    onCompletedRecord: () -> Unit,
    simple: Boolean = true,
    show: Boolean = true
) {
    if (simple) {
        BoxWithConstraints(modifier = Modifier) {
            CircularRevealContainer(
                isExpanded = show,
                modifier = Modifier.fillMaxSize(),
                baseColor = MaterialTheme.colorScheme.primaryContainer,
                expandedColor = MaterialTheme.colorScheme.primary,
                offsetInY = (this.maxHeight / 2.dp).dp
            ) {
                RecordingContent(
                    uiState = uiState,
                    onStopRecording = onStopRecording,
                    modifier = Modifier
                )

//                val infiniteTransition = rememberInfiniteTransition(label = "blink")
//                val alpha by infiniteTransition.animateFloat(
//                    initialValue = 1f,
//                    targetValue = 0f,
//                    animationSpec = infiniteRepeatable(
//                        animation = tween(800, easing = LinearEasing),
//                        repeatMode = RepeatMode.Reverse
//                    ),
//                    label = "alpha"
//                )
//
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    shape = RoundedCornerShape(24.dp),
//                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(32.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.Center
//                        ) {
//                            Text(
//                                text = "REC",
//                                fontSize = 32.sp,
//                                fontWeight = FontWeight.Bold,
//                                color = MaterialTheme.colorScheme.onSurface
//                            )
//                            Spacer(modifier = Modifier.width(12.dp))
//                            Box(
//                                modifier = Modifier
//                                    .size(18.dp)
//                                    .graphicsLayer(alpha = if (uiState.isRecording) alpha else 1f)
//                                    .background(Color.Red, CircleShape)
//                            )
//                        }
//
//                        Text(
//                            text = uiState.recordTime,
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Light,
//                            modifier = Modifier.padding(top = 8.dp)
//                        )
//
//                        Spacer(modifier = Modifier.height(40.dp))
//
//                        if (uiState.isRecording) {
//                            AudioButton(
//                                text = "Detener",
//                                onClick = onStopRecording,
//                                modifier = Modifier.fillMaxWidth()
//                            )
//                        }
//
//                        if (completedEnable) {
//                            AudioButton(
//                                text = "Transcribir",
//                                onClick = onCompletedRecord,
//                                modifier = Modifier.fillMaxWidth()
//                            )
//                        }
//
//                        if (!uiState.isRecording && !completedEnable) {
//                            AudioButton(
//                                text = "Grabar",
//                                onClick = onStartRecording,
//                                modifier = Modifier.fillMaxWidth()
//                            )
//                        }
//                    }
//                }
            }
        }
    } else if (show)
        Dialog(onDismissRequest = onDismiss) {
            val completedEnable by remember(
                uiState.isRecording,
                uiState.isPlaying,
                uiState.duration
            ) {
                derivedStateOf { !uiState.isRecording && !uiState.isPlaying && uiState.duration != "00:00:00" }
            }

            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "Rec...",
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
                            fontSize = 12.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Metering bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(4.dp)
                                )
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
                    // Time display
                    Text(
                        text = "${uiState.playTime} / ${uiState.duration}",
                        color = Color(0xFFCCCCCC),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )

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

                    OutlinedButton(
                        onClick = onCompletedRecord,
                        enabled = completedEnable,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.surface,
                            disabledContentColor = Color.Gray
                        )
                    ) {
                        Text(
                            text = "Ready",
                            color = if (completedEnable) Color.White else Color.Gray,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
}

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun RecordingContent(
    uiState: HomeUiState,
    onStopRecording: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier.fillMaxSize().padding(horizontal = 32.dp, vertical = 64.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Listening ...",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = uiState.recordTime,
                style = MaterialTheme.typography.displayMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        RecordingWaveButton(onClick = onStopRecording)
    }
}

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
fun RecordingWaveButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    val waveScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveScale"
    )

    val waveAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveAlpha"
    )

    Box(
        modifier = modifier.size(120.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    scaleX = waveScale
                    scaleY = waveScale
                    alpha = waveAlpha
                }
                .background(Color.White.copy(alpha = 0.2f), CircleShape)
        )

        Box(
            modifier = Modifier
                .fillMaxSize(0.7f)
                .graphicsLayer {
                    val secondaryScale = if (waveScale > 1.5f) waveScale - 0.5f else 1f
                    scaleX = secondaryScale
                    scaleY = secondaryScale
                    alpha = waveAlpha * 0.8f
                }
                .background(Color.White.copy(alpha = 0.15f), CircleShape)
        )

        Box(
            modifier = Modifier
                .size(72.dp)
                .styleable {
                    background(Color.Red)
                    shape(CircleShape)
                    dropShadow(
                        Shadow(
                            color = Color.Black.copy(alpha = 0.3f),
                            offset = DpOffset(0.dp, 4.dp),
                            radius = 8.dp
                        )
                    )
                }
                .clickable(
                    onClick = onClick,
                    interactionSource = null,
                    indication = null
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Pause,
                contentDescription = "Stop",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
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
            color = if (enabled) MaterialTheme.colorScheme.onBackground else Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}
