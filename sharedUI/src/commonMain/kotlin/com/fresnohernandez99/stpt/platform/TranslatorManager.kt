package com.fresnohernandez99.stpt.platform

import kotlinx.coroutines.flow.Flow

/**
 * Interfaz para gestionar la traducción offline usando ML Kit.
 */
expect class TranslatorManager {
    
    /**
     * Obtiene la lista de códigos de idiomas de los modelos ya descargados (ej. "en", "es").
     */
    suspend fun getDownloadedModels(): List<String>

    /**
     * Verifica si un modelo específico está descargado.
     */
    suspend fun checkSpecificModel(languageCode: String): Boolean

    /**
     * Inicia la descarga de un modelo de idioma.
     * @return Flow que emite el progreso o estado de la descarga.
     */
    fun downloadSpecificModel(languageCode: String): Flow<DownloadStatus>

    /**
     * Elimina un modelo descargado para liberar espacio.
     */
    suspend fun deleteSpecificModel(languageCode: String): Boolean

    /**
     * Traduce un texto entre dos idiomas. 
     * Nota: Ambos modelos deben estar descargados previamente.
     */
    suspend fun translateUsingModel(
        text: String,
        sourceLanguage: String,
        targetLanguage: String
    ): String
}

/**
 * Interfaz específica para implementación en IOs.
 */
interface TranslatorManagerIos {

    /**
     * Obtiene la lista de códigos de idiomas de los modelos ya descargados (ej. "en", "es").
     */
    suspend fun getDownloadedModels(): List<String>

    /**
     * Verifica si un modelo específico está descargado.
     */
    suspend fun checkSpecificModel(languageCode: String): Boolean

    /**
     * Inicia la descarga de un modelo de idioma.
     * @return Flow que emite el progreso o estado de la descarga.
     */
    fun downloadSpecificModel(languageCode: String, onStatusChange: (DownloadStatus) -> Unit)

    /**
     * Elimina un modelo descargado para liberar espacio.
     */
    suspend fun deleteSpecificModel(languageCode: String): Boolean

    /**
     * Traduce un texto entre dos idiomas.
     * Nota: Ambos modelos deben estar descargados previamente.
     */
    suspend fun translateUsingModel(
        text: String,
        sourceLanguage: String,
        targetLanguage: String
    ): String
}

sealed class DownloadStatus {
    object Downloading : DownloadStatus()
    object Success : DownloadStatus()
    data class Error(val message: String) : DownloadStatus()
}
