import SwiftUI
import SharedUI

@main
struct ComposeApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView().ignoresSafeArea(.all)
        }
    }
}

struct ContentView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        return MainKt.MainViewController(
            translatorManagerIos: TranslatorManagerImpl(),
            languageIdManagerIos: LanguageIdManagerIosImpl()
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        // Updates will be handled by Compose
    }
}
