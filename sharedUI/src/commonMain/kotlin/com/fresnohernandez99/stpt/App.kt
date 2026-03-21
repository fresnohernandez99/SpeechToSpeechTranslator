package com.fresnohernandez99.stpt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.fresnohernandez99.stpt.presentation.home.HomeScreen
import com.fresnohernandez99.stpt.presentation.modelSelection.ModelSelectionScreen
import com.fresnohernandez99.stpt.presentation.navigation.Destination
import com.fresnohernandez99.stpt.presentation.settings.SettingsScreen
import com.fresnohernandez99.stpt.theme.AppTheme
import com.fresnohernandez99.stpt.theme.rememberWindowSizeClass

object Arguments {
    const val NOTE_ID_PARAM = "noteId"
    const val DEFAULT_NOTE_ID = "0"
    const val ROUTE_SEPARATOR = "/"
}

@Composable
fun App(
    onThemeChanged: @Composable (isDark: Boolean) -> Unit = {}
) = AppTheme(onThemeChanged) {

    val navHostController = rememberNavController()
    val windowSize = rememberWindowSizeClass()

    NavHost(
        modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
            .systemBarsPadding(),
        navController = navHostController,
        startDestination = Destination.Home,
    ) {
        composable<Destination.Home> { backStackEntry ->
            val link: Destination.Home = backStackEntry.toRoute()

            HomeScreen(
                link = link,
                windowSize = windowSize,
                navHostController = navHostController
            )
        }
        composable<Destination.Settings> { backStackEntry ->
            val link: Destination.Settings = backStackEntry.toRoute()

            SettingsScreen(
                link = link,
                windowSize = windowSize,
                navHostController = navHostController
            )
        }
        composable<Destination.ModelSelection> { backStackEntry ->
            val link: Destination.ModelSelection = backStackEntry.toRoute()

            ModelSelectionScreen(
                link = link,
                windowSize = windowSize,
                navHostController = navHostController
            )
        }
    }
}
