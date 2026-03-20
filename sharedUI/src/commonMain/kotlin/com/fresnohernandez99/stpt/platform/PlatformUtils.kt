package com.fresnohernandez99.stpt.platform

expect class PlatformUtils {
    fun shareText(text: String)
    fun shareRecording(path: String)
    fun exportRecordingWithFilePicker(
        sourcePath: String,
        fileName: String,
        onResult: (Boolean, String?) -> Unit
    )
    fun requestStoragePermission(): Boolean
    fun exportTextWithFilePicker(
        text: String,
        fileName: String,
        onResult: (Boolean, String?) -> Unit
    )
    fun copyTextToClipboard(
        text: String,
        onResult: (Boolean, String?) -> Unit
    )
}
