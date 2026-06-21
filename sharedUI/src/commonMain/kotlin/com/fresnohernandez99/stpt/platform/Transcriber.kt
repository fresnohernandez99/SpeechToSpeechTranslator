package com.fresnohernandez99.stpt.platform

import androidx.compose.runtime.Immutable

@Immutable
expect class Transcriber {
    fun doesModelExists(modelFileName: String ): Boolean
    suspend fun initialize(modelFileName: String)
    suspend fun finish()
    suspend fun stop()
    suspend fun start(
        filePath:String, language:String,
        onProgress : (Int) -> Unit,
        onNewSegment : (Long, Long,String) -> Unit,
        onComplete : () -> Unit,
        onError : () -> Unit
    )
    fun hasRecordingPermission(): Boolean
    suspend fun requestRecordingPermission():Boolean
    fun isValidModel(modelFileName: String): Boolean
}