package chaintech.videoplayer.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import chaintech.videoplayer.model.Platform

internal expect fun formatMinSec(value: Int): String

internal expect fun formatInterval(value: Int): Int

internal expect fun formatYoutubeInterval(value: Int): Int

internal expect val youtubeProgressColor: Color

@Composable
internal expect fun LandscapeOrientation(
    isLandscape: Boolean,
    content: @Composable () -> Unit
)
internal fun extractYouTubeVideoId(url: String): String? {
    val regex = Regex(
        "https?:\\/\\/(?:www\\.|m\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?v=|embed\\/|v\\/|e\\/|live\\/|shorts\\/|user\\/))([^&#?\\n]+)"
    )
    return regex.find(url)?.groups?.get(1)?.value
}

internal fun isLiveStream(url: String): Boolean {
    return url.endsWith(".m3u8")
}

fun isDesktop() : Boolean {
    return isPlatform() == Platform.Desktop
}

expect fun isPlatform(): Platform