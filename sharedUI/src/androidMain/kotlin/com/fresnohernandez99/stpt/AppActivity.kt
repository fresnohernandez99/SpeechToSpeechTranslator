package com.fresnohernandez99.stpt

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat
import com.fresnohernandez99.stpt.di.initKoinApplication
import io.github.hyochan.audio.initializeAudioRecorderPlayer
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initKoinApplication {
            androidContext(this@AppActivity)
            androidLogger()
        }
        // Initialize audio recorder player with context
        initializeAudioRecorderPlayer(this)
        setContent {
            App(onThemeChanged = { ThemeChanged(it) })
        }
    }
}

@Composable
private fun ThemeChanged(isDark: Boolean) {
    val view = LocalView.current
    LaunchedEffect(isDark) {
        val window = (view.context as Activity).window
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = isDark
            isAppearanceLightNavigationBars = isDark
        }
    }
}
