package com.fresnohernandez99.stpt.utils

import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Streaming audio chunker that reads chunks directly from WAV files
 * without loading the entire file into memory
 */
class StreamingAudioChunker {

    companion object {
        // 10MB chunks (approximately 10 minutes of audio at 16kHz mono)
        const val CHUNK_SIZE_BYTES = 10 * 1024 * 1024 // 10MB
        const val OVERLAP_SIZE_BYTES = 1 * 1024 * 1024 // 1MB overlap (10% overlap)

        // Minimum chunk size to ensure quality transcription
        const val MIN_CHUNK_SIZE_BYTES = 5 * 1024 * 1024 // 5MB minimum
        
        const val TARGET_SAMPLE_RATE = 16000 // Frecuencia requerida por Whisper
    }

    // Reusable arrays to avoid repeated allocations
    private var reusableFloatArray: FloatArray? = null
    private var reusableResampledArray: FloatArray? = null
    private var reusableByteArray: ByteArray? = null

    /**
     * Reads WAV file header and returns metadata
     */
    private fun readWavHeader(file: RandomAccessFile): WavHeader {
        val fileLength = file.length()
        val header = ByteArray(44)
        file.readFully(header)

        val buffer = ByteBuffer.wrap(header)
        buffer.order(ByteOrder.LITTLE_ENDIAN)

        // Check if it's a valid WAV file
        val riff = String(header, 0, 4)
        val wave = String(header, 8, 4)
        if (riff != "RIFF" || wave != "WAVE") {
            throw IllegalArgumentException("Not a valid WAV file")
        }

        val channels = buffer.getShort(22).toInt()
        val sampleRate = buffer.getInt(24)
        val bitsPerSample = buffer.getShort(34).toInt()
        
        // Corregir dataSize si el encabezado es inválido o excede el archivo
        var dataSize = buffer.getInt(40)
        val actualDataSize = (fileLength - 44).toInt()
        if (dataSize !in 1..actualDataSize) {
            println("WavHeader: dataSize in header ($dataSize) is invalid. Using actual data size: $actualDataSize")
            dataSize = actualDataSize
        }

        return WavHeader(
            channels = channels,
            sampleRate = sampleRate,
            bitsPerSample = bitsPerSample,
            dataSize = dataSize
        )
    }

    /**
     * Splits WAV file into overlapping chunks without loading entire file into memory
     */
    fun splitWavFileIntoChunks(
        filePath: String,
        chunkSizeBytes: Int = CHUNK_SIZE_BYTES,
        overlapSizeBytes: Int = OVERLAP_SIZE_BYTES
    ): MutableList<StreamingAudioChunk> {
        val file = RandomAccessFile(filePath, "r")

        file.use { raf ->
            val header = readWavHeader(raf)
            val chunks = mutableListOf<StreamingAudioChunk>()
            val totalDataSize = header.dataSize

            println("Splitting WAV file: $totalDataSize bytes into chunks")

            var currentOffset = 44L // Skip WAV header
            var chunkIndex = 0

            while (currentOffset < 44 + totalDataSize) {
                val remainingBytes = (44 + totalDataSize) - currentOffset
                if (remainingBytes <= 0) break
                
                val currentChunkSize = minOf(chunkSizeBytes.toLong(), remainingBytes)

                // Calculate overlap for next chunk
                val chunkStartOffset = if (chunkIndex > 0) {
                    maxOf(44L, currentOffset - overlapSizeBytes)
                } else {
                    currentOffset
                }

                val chunkEndOffset = currentOffset + currentChunkSize

                chunks.add(
                    StreamingAudioChunk(
                        chunkIndex = chunkIndex,
                        filePath = filePath,
                        startOffset = chunkStartOffset,
                        endOffset = chunkEndOffset,
                        header = header,
                        isFirstChunk = chunkIndex == 0,
                        isLastChunk = currentOffset + currentChunkSize >= 44 + totalDataSize
                    )
                )

                chunkIndex++
                currentOffset += maxOf(1, chunkSizeBytes - overlapSizeBytes)

                if (remainingBytes - (chunkSizeBytes - overlapSizeBytes) < MIN_CHUNK_SIZE_BYTES) {
                    break
                }
            }

            println("Created ${chunks.size} streaming chunks")
            return chunks
        }
    }

    /**
     * Reads a specific chunk from the WAV file, converts to mono and resamples to 16kHz if needed
     */
    fun readChunkData(chunk: StreamingAudioChunk): FloatArray {
        val file = RandomAccessFile(chunk.filePath, "r")

        file.use { raf ->
            raf.seek(chunk.startOffset)

            val available = (raf.length() - chunk.startOffset).toInt()
            val requestedSize = (chunk.endOffset - chunk.startOffset).toInt()
            val chunkSize = minOf(requestedSize, available)

            if (chunkSize <= 0) return FloatArray(0)

            val buffer = getReusableByteArray(chunkSize)
            raf.readFully(buffer, 0, chunkSize)

            // 1. Convertir bytes a FloatArray (ya maneja Stereo a Mono promediando)
            val originalFloats = convertBytesToFloatArrayReusable(buffer, chunkSize, chunk.header)
            
            // 2. Si la frecuencia no es 16000, remuestrear antes de entregar a Whisper
            val result = if (chunk.header.sampleRate != TARGET_SAMPLE_RATE) {
                val inputSamples = chunkSize / (chunk.header.channels * (chunk.header.bitsPerSample / 8))
                resampleToTarget(originalFloats, inputSamples, chunk.header.sampleRate)
            } else {
                originalFloats
            }

            // Clear the byte buffer from memory
            buffer.fill(0, 0, chunkSize)

            return result
        }
    }

    /**
     * Resamples audio data to TARGET_SAMPLE_RATE (16kHz) using linear interpolation
     */
    private fun resampleToTarget(input: FloatArray, inputSamples: Int, sourceRate: Int): FloatArray {
        val ratio = sourceRate.toDouble() / TARGET_SAMPLE_RATE.toDouble()
        val outputSamples = (inputSamples / ratio).toInt()
        val result = getReusableResampledArray(outputSamples)

        for (i in 0 until outputSamples) {
            val sourceIndex = i * ratio
            val indexInt = sourceIndex.toInt()
            val fraction = (sourceIndex - indexInt).toFloat()

            if (indexInt + 1 < inputSamples) {
                // Linear interpolation
                result[i] = input[indexInt] * (1f - fraction) + input[indexInt + 1] * fraction
            } else if (indexInt < inputSamples) {
                result[i] = input[indexInt]
            }
        }
        return result
    }

    /**
     * Gets or creates a reusable ByteArray of the required size
     */
    private fun getReusableByteArray(requiredSize: Int): ByteArray {
        val current = reusableByteArray
        return if (current != null && current.size >= requiredSize) {
            current
        } else {
            val newArray = ByteArray(requiredSize)
            reusableByteArray = newArray
            newArray
        }
    }

    /**
     * Gets or creates a reusable FloatArray of the required size
     */
    private fun getReusableFloatArray(requiredSize: Int): FloatArray {
        val current = reusableFloatArray
        return if (current != null && current.size >= requiredSize) {
            current
        } else {
            val newArray = FloatArray(requiredSize)
            reusableFloatArray = newArray
            newArray
        }
    }

    /**
     * Gets or creates a reusable FloatArray for resampled data
     */
    private fun getReusableResampledArray(requiredSize: Int): FloatArray {
        val current = reusableResampledArray
        return if (current != null && current.size >= requiredSize) {
            current
        } else {
            val newArray = FloatArray(requiredSize)
            reusableResampledArray = newArray
            newArray
        }
    }

    /**
     * Clears reusable arrays from memory
     */
    fun clearReusableArrays() {
        reusableFloatArray = null
        reusableResampledArray = null
        reusableByteArray = null
    }

    /**
     * Gets current array sizes for debugging
     */
    fun getReusableArraySizes(): Pair<Int, Int> {
        return Pair(
            reusableFloatArray?.size ?: 0,
            reusableByteArray?.size ?: 0
        )
    }

    /**
     * Converts byte array to FloatArray based on WAV format (reusable version)
     * Handles Stereo to Mono conversion by averaging channels
     */
    private fun convertBytesToFloatArrayReusable(
        buffer: ByteArray,
        bytesRead: Int,
        header: WavHeader
    ): FloatArray {
        val bytesPerSample = header.bitsPerSample / 8
        val samplesCount = bytesRead / (header.channels * bytesPerSample)

        val result = getReusableFloatArray(samplesCount)
        val byteBuffer = ByteBuffer.wrap(buffer, 0, bytesRead)
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN)

        for (i in 0 until samplesCount) {
            val sample = when (header.channels) {
                1 -> {
                    // Mono
                    val sampleValue = byteBuffer.short
                    (sampleValue / 32767.0f).coerceIn(-1f..1f)
                }
                else -> {
                    // Stereo - average the channels
                    val left = byteBuffer.short.toInt()
                    val right = byteBuffer.short.toInt()
                    ((left + right) / 2.0f / 32767.0f).coerceIn(-1f..1f)
                }
            }
            result[i] = sample
        }

        return result
    }
}

/**
 * WAV file header information
 */
data class WavHeader(
    val channels: Int,
    val sampleRate: Int,
    val bitsPerSample: Int,
    val dataSize: Int
)

/**
 * Represents a streaming audio chunk with file position information
 */
data class StreamingAudioChunk(
    val chunkIndex: Int,
    val filePath: String,
    val startOffset: Long,
    val endOffset: Long,
    val header: WavHeader,
    val isFirstChunk: Boolean,
    val isLastChunk: Boolean
) {
    val sizeBytes: Long get() = endOffset - startOffset
    val durationSeconds: Double get() = sizeBytes / (header.sampleRate * header.channels * (header.bitsPerSample / 8.0))
}

data class AudioChunk(
    val startSample: Int,
    val endSample: Int,
    val data: FloatArray
) {
    val durationMs: Long get() = ((endSample - startSample + 1) / 16.0).toLong()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AudioChunk) return false

        if (startSample != other.startSample) return false
        if (endSample != other.endSample) return false
        return data.contentEquals(other.data)
    }

    override fun hashCode(): Int {
        var result = startSample
        result = 31 * result + endSample
        result = 31 * result + data.contentHashCode()
        return result
    }
}


/**
 * Represents the transcription result for a single chunk
 */
data class ChunkTranscriptionResult(
    val chunk: AudioChunk,
    val text: String,
    val segments: List<TranscriptionSegment> = emptyList()
)

/**
 * Represents a single transcription segment with timing information
 */
data class TranscriptionSegment(
    val startMs: Long,
    val endMs: Long,
    val text: String
)
