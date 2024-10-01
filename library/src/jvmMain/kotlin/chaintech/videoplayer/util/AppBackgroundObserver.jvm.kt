package chaintech.videoplayer.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
internal actual class AppBackgroundObserver {
    actual fun observe(callback: () -> Unit) {
    }

    actual fun removeObserver() {
    }
}

@Composable
internal actual fun rememberAppBackgroundObserver(): AppBackgroundObserver {
    return remember { AppBackgroundObserver() }
}