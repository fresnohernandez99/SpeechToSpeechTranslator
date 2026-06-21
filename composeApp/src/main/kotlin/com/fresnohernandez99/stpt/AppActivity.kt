package com.fresnohernandez99.stpt

import android.app.Activity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat
import com.fresnohernandez99.stpt.di.initKoinApplication
import io.github.hyochan.audio.initializeAudioRecorderPlayer
import io.kmpbits.splash.SplashActivity
import kotlinx.coroutines.delay
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import kotlin.time.Duration.Companion.milliseconds

class AppActivity : SplashActivity() {

    override fun onPreCreate() {
        enableEdgeToEdge()
        initKoinApplication {
            androidContext(this@AppActivity)
            androidLogger()
        }
        // Initialize audio recorder player with context
        initializeAudioRecorderPlayer(this)
    }

    override suspend fun isReady(): Boolean {
        delay(200.milliseconds)
        return true
    }

    override fun onFinished() {
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
