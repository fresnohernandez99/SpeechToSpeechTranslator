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
import com.fresnohernandez99.stpt.di.appModule
import com.fresnohernandez99.stpt.presentation.home.HomeScreen
import com.fresnohernandez99.stpt.presentation.navigation.Destination
import com.fresnohernandez99.stpt.theme.AppTheme
import com.fresnohernandez99.stpt.theme.rememberWindowSizeClass
import org.koin.compose.KoinApplication
import org.koin.dsl.koinConfiguration

@Composable
fun App(
    onThemeChanged: @Composable (isDark: Boolean) -> Unit = {}
) = AppTheme(onThemeChanged) {

    val navHostController = rememberNavController()
    val windowSize = rememberWindowSizeClass()

    KoinApplication(
        configuration = koinConfiguration(declaration = { modules(appModule) }),
        content = {
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
            }
        }
    )
}
