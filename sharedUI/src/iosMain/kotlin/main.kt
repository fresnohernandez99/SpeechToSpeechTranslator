import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.ComposeUIViewController
import com.fresnohernandez99.stpt.App
import com.fresnohernandez99.stpt.di.init
import com.fresnohernandez99.stpt.platform.TranslatorManagerIos
import io.kmpbits.splash.SplashConfig
import kotlinx.coroutines.delay
import org.koin.compose.KoinApplication
import org.koin.dsl.koinConfiguration
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.UIViewController
import platform.UIKit.setStatusBarStyle
import kotlin.time.Duration.Companion.milliseconds

fun MainViewController(
    translatorManagerIos: TranslatorManagerIos
): UIViewController = ComposeUIViewController {
    KoinApplication(
        configuration = koinConfiguration(declaration = { init(translatorManagerIos) }),
        content = {
            SplashConfig(
                isReady = {
                    delay(500.milliseconds)
                    true
                }
            ) {
                App(onThemeChanged = { ThemeChanged(it) })
            }

        })
}

@Composable
private fun ThemeChanged(isDark: Boolean) {
    LaunchedEffect(isDark) {
        UIApplication.sharedApplication.setStatusBarStyle(
            if (isDark) UIStatusBarStyleDarkContent else UIStatusBarStyleLightContent
        )
    }
}