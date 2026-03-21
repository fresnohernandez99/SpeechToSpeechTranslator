import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.ComposeUIViewController
import com.fresnohernandez99.stpt.App
import com.fresnohernandez99.stpt.di.init
import com.fresnohernandez99.stpt.platform.TranslatorManagerIos
import org.koin.compose.KoinApplication
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.UIViewController
import platform.UIKit.setStatusBarStyle

fun MainViewController(
    translatorManagerIos: TranslatorManagerIos
): UIViewController = ComposeUIViewController {
    KoinApplication(application = {
        init(translatorManagerIos)
    }) {
        App(onThemeChanged = { ThemeChanged(it) })
    }
}

@Composable
private fun ThemeChanged(isDark: Boolean) {
    LaunchedEffect(isDark) {
        UIApplication.sharedApplication.setStatusBarStyle(
            if (isDark) UIStatusBarStyleDarkContent else UIStatusBarStyleLightContent
        )
    }
}