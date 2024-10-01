package chaintech.videoplayer.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import platform.Foundation.NSNotificationCenter
import platform.UIKit.UIApplicationWillResignActiveNotification
import platform.darwin.NSObjectProtocol

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class AppBackgroundObserver {
    private val notificationCenter = NSNotificationCenter.defaultCenter()
    private var observerDidEnterBackground: NSObjectProtocol? = null

    actual fun observe(callback: () -> Unit) {
        observerDidEnterBackground = notificationCenter.addObserverForName(
            name = UIApplicationWillResignActiveNotification,
            `object` = null,
            queue = null
        ) { _ ->
            callback()
        }
    }

    actual fun removeObserver() {
        notificationCenter.removeObserver(observerDidEnterBackground!!)
        observerDidEnterBackground = null
    }
}

@Composable
actual fun rememberAppBackgroundObserver(): AppBackgroundObserver {
    return remember { AppBackgroundObserver() }
}