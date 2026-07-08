package com.fresnohernandez99.stpt.fakes

import io.github.hyochan.audio.AudioMeteringInfo
import io.github.hyochan.audio.PlaybackProgress
import io.github.hyochan.audio.RecordingInfo
import io.github.hyochan.audio.RecordingProgress
import io.github.hyochan.audio.AudioRecorderPlayer
import io.github.hyochan.audio.AudioRecorderPlayerProperties
import io.github.hyochan.audio.RecorderAudioSet
import io.github.hyochan.audio.AudioSource

class FakeAudioRecorderPlayer : AudioRecorderPlayer {
    override fun addAudioMeteringListener(listener: (AudioMeteringInfo) -> Unit) {}
    override fun addPlaybackListener(listener: (PlaybackProgress) -> Unit) {}
    override fun addRecordingListener(listener: (RecordingProgress) -> Unit) {}
    override suspend fun getRecordingInfo(): Result<RecordingInfo?> = Result.success(null)
    override suspend fun pausePlaying(): Result<Unit> = Result.success(Unit)
    override suspend fun pauseRecording(): Result<Unit> = Result.success(Unit)
    override fun removeListeners() {}
    override fun removeAudioMeteringListener() {}
    override suspend fun resumePlaying(): Result<Unit> = Result.success(Unit)
    override suspend fun resumeRecording(): Result<Unit> = Result.success(Unit)
    override suspend fun seekTo(position: Long): Result<Unit> = Result.success(Unit)
    override fun setPlayerProperties(properties: AudioRecorderPlayerProperties) {}
    override fun setRecorderProperties(audioSet: RecorderAudioSet) {}
    override suspend fun setPlaybackSpeed(speed: Float): Result<Unit> = Result.success(Unit)
    override suspend fun setVolume(volume: Float): Result<Unit> = Result.success(Unit)
    override suspend fun startPlaying(filePath: String?): Result<Unit> = Result.success(Unit)
    override suspend fun startPlaying(source: AudioSource): Result<Unit> = Result.success(Unit)
    override suspend fun startRecording(filePath: String?): Result<String> = Result.success("path")
    override suspend fun stopPlaying(): Result<Unit> = Result.success(Unit)
    override suspend fun stopRecording(): Result<String> = Result.success("path")
}
