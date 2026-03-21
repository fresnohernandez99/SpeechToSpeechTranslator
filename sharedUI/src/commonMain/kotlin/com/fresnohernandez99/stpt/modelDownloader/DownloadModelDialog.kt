package com.fresnohernandez99.stpt.modelDownloader

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import speechtospeechtranslator.sharedui.generated.resources.Res
import speechtospeechtranslator.sharedui.generated.resources.cancel
import speechtospeechtranslator.sharedui.generated.resources.download
import speechtospeechtranslator.sharedui.generated.resources.download_required
import speechtospeechtranslator.sharedui.generated.resources.download_required_for_hindi
import speechtospeechtranslator.sharedui.generated.resources.file_model_english
import speechtospeechtranslator.sharedui.generated.resources.file_model_hindi
import speechtospeechtranslator.sharedui.generated.resources.file_size_approx
import speechtospeechtranslator.sharedui.generated.resources.for_accurate_transcription
import speechtospeechtranslator.sharedui.generated.resources.take_few_minutes

@Composable
fun DownloadModelDialog(
    onDownload: () -> Unit,
    onCancel: () -> Unit,
    transcriptionModel: TranscriptionModel,
    modifier: Modifier = Modifier
) {
    val fileInfo: String = if (transcriptionModel.getModelDownloadType() == HINDI_MODEL) {
        stringResource(Res.string.file_model_hindi)
    } else {
        stringResource(Res.string.file_model_english)
    }

    val downloadRequired: String = if (transcriptionModel.getModelDownloadType() == HINDI_MODEL) {
        stringResource(Res.string.download_required_for_hindi)
    } else {
        stringResource(Res.string.download_required)
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onCancel,
        title = {
            Text(text = downloadRequired)
        },
        text = {
            Column {
                Text(stringResource(Res.string.for_accurate_transcription))
                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(Res.string.take_few_minutes))
                Spacer(modifier = Modifier.height(8.dp))
                Text(fileInfo)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    stringResource(
                        Res.string.file_size_approx,
                        transcriptionModel.getModelDownloadSize()
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDownload,
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White
                )
            ) {
                Text(stringResource(Res.string.download))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancel
            ) {
                Text(stringResource(Res.string.cancel))
            }
        }
    )
}