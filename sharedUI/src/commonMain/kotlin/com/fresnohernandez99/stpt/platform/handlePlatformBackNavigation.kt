package com.fresnohernandez99.stpt.platform

import androidx.compose.runtime.Composable

@Composable
expect fun HandlePlatformBackNavigation(enabled: Boolean, onBack: () -> Unit)