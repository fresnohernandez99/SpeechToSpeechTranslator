package com.fresnohernandez99.stpt.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination {
    @Serializable
    data class Home(val typed: String = "") : Destination
    @Serializable
    data object Settings : Destination
    @Serializable
    data object ModelSelection : Destination
    @Serializable
    data class Transcription(val audioPath: String = "") : Destination
}
