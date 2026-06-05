import SwiftUI
import InitKoinKt

@main
struct iOSApp: App {

    init() {
        InitKoinKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}