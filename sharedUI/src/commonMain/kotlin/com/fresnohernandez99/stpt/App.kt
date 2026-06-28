package com.fresnohernandez99.stpt

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.fresnohernandez99.stpt.presentation.components.LoadingView
import com.fresnohernandez99.stpt.presentation.dictsManage.DictsManageScreen
import com.fresnohernandez99.stpt.presentation.home.HomeScreen
import com.fresnohernandez99.stpt.presentation.languageSelection.LanguageSelectionScreen
import com.fresnohernandez99.stpt.presentation.modelSelection.ModelSelectionScreen
import com.fresnohernandez99.stpt.presentation.navigation.Destination
import com.fresnohernandez99.stpt.presentation.navigation.LocalNavController
import com.fresnohernandez99.stpt.presentation.onboarding.OnboardingScreen
import com.fresnohernandez99.stpt.presentation.settings.SettingsScreen
import com.fresnohernandez99.stpt.theme.AppTheme
import com.fresnohernandez99.stpt.theme.LocalWindowSizeHelper
import com.fresnohernandez99.stpt.theme.rememberWindowSizeClass
import com.fresnohernandez99.stpt.transcription.TranscriptionScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    onThemeChanged: @Composable (isDark: Boolean) -> Unit = {},
    viewModel: InitViewModel = koinViewModel()
) = AppTheme(onThemeChanged) {

    val navHostController = rememberNavController()
    val windowSize = rememberWindowSizeClass()
    val uiState by viewModel.uiState.collectAsState()

    CompositionLocalProvider(
        LocalNavController provides navHostController,
        LocalWindowSizeHelper provides windowSize
    ) {

        when (uiState) {
            InitUiState.Loading -> {
                LoadingView()
            }

            else -> {
                val startDestination = if (uiState == InitUiState.Resolved) {
                    Destination.Home()
                } else {
                    Destination.Onboarding
                }

                NavHost(
                    modifier = Modifier,
                    navController = navHostController,
                    startDestination = startDestination,
                    enterTransition = { fadeIn() + slideInHorizontally() },
                    exitTransition = { fadeOut() + slideOutHorizontally() }
                ) {
                    composable<Destination.Onboarding> { backStackEntry ->
                        val link: Destination.Onboarding = backStackEntry.toRoute()

                        OnboardingScreen(
                            link = link
                        )
                    }
                    composable<Destination.Home> { backStackEntry ->
                        val link: Destination.Home = backStackEntry.toRoute()

                        HomeScreen(
                            link = link
                        )
                    }
                    composable<Destination.Settings> { backStackEntry ->
                        val link: Destination.Settings = backStackEntry.toRoute()

                        SettingsScreen(
                            link = link
                        )
                    }
                    composable<Destination.ModelSelection> { backStackEntry ->
                        val link: Destination.ModelSelection = backStackEntry.toRoute()

                        ModelSelectionScreen(
                            link = link
                        )
                    }
                    composable<Destination.Transcription> { backStackEntry ->
                        val link: Destination.Transcription = backStackEntry.toRoute()

                        TranscriptionScreen(
                            link = link
                        )
                    }
                    composable<Destination.DictsManage> { backStackEntry ->
                        val link: Destination.DictsManage = backStackEntry.toRoute()

                        DictsManageScreen(
                            link = link
                        )
                    }
                    composable<Destination.LanguageSelection> { backStackEntry ->
                        val link: Destination.LanguageSelection = backStackEntry.toRoute()

                        LanguageSelectionScreen(
                            link = link
                        )
                    }
                }
            }
        }
    }
}
