package com.fresnohernandez99.stpt.presentation.onboarding

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.DrawableResource

@Immutable
data class OnboardingPage(
    val title: String,
    val description: String,
    val imageRes: DrawableResource
)