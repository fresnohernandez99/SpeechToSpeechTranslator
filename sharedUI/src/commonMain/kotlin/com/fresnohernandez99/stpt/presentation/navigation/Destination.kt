package com.fresnohernandez99.stpt.presentation.navigation

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Serializable
@Immutable
sealed interface Destination {
    @Serializable
    @Immutable
    data class Home(val typed: String = "") : Destination
    @Serializable
    @Immutable
    data object Settings : Destination
    @Serializable
    @Immutable
    data object ModelSelection : Destination
    @Serializable
    @Immutable
    data class Transcription(val audioPath: String = "") : Destination
    @Serializable
    @Immutable
    data object DictsManage : Destination
}
