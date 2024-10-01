package chaintech.videoplayer.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import chaintech.videoplayer.model.Platform
import platform.Foundation.NSString
import platform.Foundation.NSURL
import platform.Foundation.stringWithFormat
import platform.UIKit.UIApplication
import platform.UIKit.UIInterfaceOrientationMaskLandscapeRight
import platform.UIKit.UIInterfaceOrientationMaskPortrait
import platform.UIKit.UIWindowSceneGeometryPreferencesIOS

actual fun formatMinSec(value: Int): String {
    val hour = (value / 3600)
    val remainingSecondsAfterHours = (value % 3600)
    val minutes = remainingSecondsAfterHours / 60
    val seconds = remainingSecondsAfterHours % 60

    val strHour : String = if (hour > 0) { NSString.stringWithFormat(format = "%02d:", hour)
    } else { "" }
    val strMinutes : String = NSString.stringWithFormat(format = "%02d:", minutes)
    val strSeconds : String = NSString.stringWithFormat(format = "%02d", seconds)

    return "${strHour}${strMinutes}${strSeconds}"
}

actual fun formatInterval(value: Int): Int {
    return value
}

@Composable
actual fun LandscapeOrientation(
    isLandscape: Boolean,
    content: @Composable () -> Unit
) {

    DisposableEffect(isLandscape) {
        val windowScene = UIApplication.sharedApplication.keyWindow?.windowScene
        val orientation = if (isLandscape) {
            UIInterfaceOrientationMaskLandscapeRight
        } else {
            UIInterfaceOrientationMaskPortrait
        }
        windowScene?.requestGeometryUpdateWithPreferences(UIWindowSceneGeometryPreferencesIOS().apply {
            interfaceOrientations = orientation
        }, errorHandler = null)
        onDispose {
            windowScene?.requestGeometryUpdateWithPreferences(UIWindowSceneGeometryPreferencesIOS().apply {
                interfaceOrientations = UIInterfaceOrientationMaskPortrait
            }, errorHandler = null)
        }
    }
    content()
}


actual fun formatYoutubeInterval(value: Int): Int {
    return value
}

actual val youtubeProgressColor: Color
    get() = Color(0xFFdddddd)

internal fun createUrl(url: String): NSURL? {
    return if (url.startsWith("http://") || url.startsWith("https://")) {
        NSURL.URLWithString(url)
    } else {
        NSURL.fileURLWithPath(url)
    }
}

actual fun isPlatform(): Platform {
    return Platform.Ios
}