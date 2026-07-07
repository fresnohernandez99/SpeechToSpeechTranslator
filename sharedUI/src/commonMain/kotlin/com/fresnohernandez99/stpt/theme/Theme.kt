package com.fresnohernandez99.stpt.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Main,
    secondary = TextStyle,
    tertiary = SecondlyText,
    surface = Ghost,
    surfaceDim = Text,
    background = Dark,
    primaryContainer = PrimaryContainerLight,
    secondaryContainer = SecondaryContainerLight,
    inversePrimary = Main,
    error = ErrorRed,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = MainDark,
    secondary = TextStyle,
    tertiary = SecondlyText,
    surface = Ghost,
    surfaceDim = Text,
    background = Dark,
    primaryContainer = PrimaryContainerDark,
    secondaryContainer = SecondaryContainerDark,
    inversePrimary = IconsColorDark,
    error = ErrorRedDark,
    onError = Color.Black
)

internal val LocalThemeIsDark = compositionLocalOf { mutableStateOf(true) }

@Composable
internal fun AppTheme(
    onThemeChanged: @Composable (isDark: Boolean) -> Unit,
    bigSize: Boolean = false,
    content: @Composable () -> Unit
) {
    val systemIsDark = isSystemInDarkTheme()
    val isDarkState = remember(systemIsDark) { mutableStateOf(systemIsDark) }
    CompositionLocalProvider(
        LocalThemeIsDark provides isDarkState
    ) {
        val isDark by isDarkState
        val currentScheme = if (isDark) DarkColorScheme else LightColorScheme
        onThemeChanged(!isDark)
        MaterialTheme(
            colorScheme = currentScheme,
            typography = if (bigSize) appTypographyBig(currentScheme) else appTypography(
                currentScheme
            ),
            content = { Surface(content = content) }
        )
    }
}
