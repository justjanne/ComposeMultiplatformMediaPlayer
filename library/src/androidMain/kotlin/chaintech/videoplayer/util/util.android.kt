package chaintech.videoplayer.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import chaintech.videoplayer.model.Platform
import java.util.concurrent.TimeUnit

@SuppressLint("DefaultLocale")
actual fun formatMinSec(value: Int): String {
    return if (value == 0) {
        "00:00"
    } else {
        // Convert value from milliseconds to total seconds
        val totalSeconds = TimeUnit.MILLISECONDS.toSeconds(value.toLong())

        // Calculate hours, minutes, and seconds
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        // Format the output string
        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }
}

actual fun formatInterval(value: Int): Int {
    return value * 1000
}

@Composable
actual fun LandscapeOrientation(
    isLandscape: Boolean,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity

    DisposableEffect(isLandscape) {
        activity?.requestedOrientation = if (isLandscape) { ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE } else { ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED }
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    content()
}

actual fun formatYoutubeInterval(value: Int): Int {
    return value * 1000
}

actual val youtubeProgressColor: Color
    get() = Color(0xFF343434)

internal fun isHlsUrl(url: String): Boolean {
    return url.endsWith(".m3u8") || url.contains("application/vnd.apple.mpegurl")
}

actual fun isPlatform(): Platform {
    return Platform.Android
}