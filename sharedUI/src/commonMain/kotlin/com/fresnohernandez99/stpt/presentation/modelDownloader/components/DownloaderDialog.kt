package com.fresnohernandez99.stpt.presentation.modelDownloader.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.fresnohernandez99.stpt.presentation.modelDownloader.DownloaderUiState
import com.module.notelycompose.modelDownloader.TranscriptionModel


@Composable
fun DownloaderDialog(
    transcriptionModel: TranscriptionModel,
    downloaderUiState: DownloaderUiState,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false,
            usePlatformDefaultWidth = true
        )
    ) {
        Surface(
            modifier = Modifier,
            shape = RoundedCornerShape(16.dp), // Rounded corners
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.Start,

                ) {
                Text(
                    "Downloading ${transcriptionModel.description}"
                )
                LinearProgressIndicator(
                    progress = { (downloaderUiState.progress / 100) },
                    modifier = Modifier.padding(vertical = 12.dp).fillMaxWidth(),
                    color = ProgressIndicatorDefaults.linearColor,
                    trackColor = ProgressIndicatorDefaults.linearTrackColor,
                    strokeCap = StrokeCap.Round,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        downloaderUiState.downloaded
                    )
                    Text(
                        "/",
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    Text(
                        downloaderUiState.total
                    )
                }
            }
        }
    }
}





