package com.fresnohernandez99.stpt.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination {
    @Serializable
    data object Home : Destination
}
