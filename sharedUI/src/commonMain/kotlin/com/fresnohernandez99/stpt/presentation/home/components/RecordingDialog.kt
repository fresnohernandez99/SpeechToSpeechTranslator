package com.fresnohernandez99.stpt.presentation.home.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import com.fresnohernandez99.stpt.audio.ui.recorder.RecordingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordingDialog(
    onDismiss: () -> Unit,
    onUpdateRecordingPath: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        RecordingScreen(
            noteId = 1,
            navigateBack = onDismiss,
            onUpdateRecordingPath = onUpdateRecordingPath
        )
    }
}