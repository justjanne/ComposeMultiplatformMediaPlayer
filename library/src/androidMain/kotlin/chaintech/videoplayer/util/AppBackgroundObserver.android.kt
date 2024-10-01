package chaintech.videoplayer.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
internal actual class AppBackgroundObserver {
    private val lifecycleOwner: LifecycleOwner = ProcessLifecycleOwner.get()
    private val lifecycle = lifecycleOwner.lifecycle
    private var observer: LifecycleEventObserver? = null

    actual fun observe(callback: () -> Unit) {
        observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {}
                Lifecycle.Event.ON_STOP -> callback()
                else -> { /* no-op */ }
            }
        }
        lifecycle.addObserver(observer!!)
    }

    actual fun removeObserver() {
        observer?.let {
            lifecycle.removeObserver(it)
            observer = null
        }
    }
}

@Composable
internal actual fun rememberAppBackgroundObserver(): AppBackgroundObserver {
    return remember { AppBackgroundObserver() }
}
