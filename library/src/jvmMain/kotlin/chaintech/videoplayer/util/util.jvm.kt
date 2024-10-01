package chaintech.videoplayer.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import chaintech.videoplayer.model.Platform

@Suppress("DefaultLocale")
internal actual fun formatMinSec(value: Int): String {
    return if (value == 0) {
        "00:00"
    } else {
        val hour = value / 3600
        val remainingSecondsAfterHours = value % 3600
        val minutes = remainingSecondsAfterHours / 60
        val seconds = remainingSecondsAfterHours % 60

        // Format the output string
        return if (hour > 0) {
            String.format("%02d:%02d:%02d", hour, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }
}

internal actual fun formatInterval(value: Int): Int {
    return value
}

internal actual fun formatYoutubeInterval(value: Int): Int {
    return value
}

internal actual val youtubeProgressColor: Color
    get() = Color(0xFFdddddd)

@Composable
internal actual fun LandscapeOrientation(
    isLandscape: Boolean,
    content: @Composable () -> Unit
) {
    content()
}

actual fun isPlatform(): Platform {
    return Platform.Desktop
}