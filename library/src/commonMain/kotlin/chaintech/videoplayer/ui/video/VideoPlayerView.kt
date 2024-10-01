package chaintech.videoplayer.ui.video

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import chaintech.videoplayer.model.PlayerConfig
import chaintech.videoplayer.ui.youtube.YoutubePlayerWithControl
import chaintech.videoplayer.util.LandscapeOrientation
import chaintech.videoplayer.util.extractYouTubeVideoId
import chaintech.videoplayer.util.isDesktop
import kotlinx.coroutines.delay

@Composable
fun VideoPlayerView(
    modifier: Modifier = Modifier, // Modifier for the composable
    url: String, // URL of the video
    playerConfig: PlayerConfig = PlayerConfig() // Configuration for the player
) {
    var isPause by remember { mutableStateOf(false) } // State for pausing/resuming video
    var showControls by remember { mutableStateOf(true) } // State for showing/hiding controls
    var isSeekbarSliding = false // Flag for indicating if the seek bar is being slid
    var isFullScreen by remember { mutableStateOf(false) }

    playerConfig.isPause?.let {
        isPause = it
    }
    LaunchedEffect(url){
        if(playerConfig.isPause == null) {
            isPause = false
        }
    }

    // Auto-hide controls if enabled
    if(playerConfig.isAutoHideControlEnabled) {
        LaunchedEffect(showControls) {
            if (showControls) {
                delay(timeMillis = (playerConfig.controlHideIntervalSeconds * 1000).toLong()) // Delay hiding controls
                if (isSeekbarSliding.not()) {
                    showControls = false // Hide controls if seek bar is not being slid
                }
            }
        }
    }
    val videoUrl = extractYouTubeVideoId(url)

    LandscapeOrientation(isFullScreen) {
        if (videoUrl == null) {
            if (isDesktop()) {
                // Video player with control
                DesktopVideoPlayer(
                    modifier = if (isFullScreen) {
                        Modifier.fillMaxSize()
                    } else {
                        modifier
                    },
                    url = url, // URL of the video
                    playerConfig = playerConfig, // Player configuration
                    isPause = isPause, // Flag indicating if the video is paused
                    onPauseToggle = {
                        playerConfig.pauseCallback?.invoke(isPause.not())
                        isPause = isPause.not()
                    }
                )
            } else {
                // Video player with control
                VideoPlayerWithControl(
                    modifier = if (isFullScreen) {
                        Modifier.fillMaxSize()
                    } else {
                        modifier
                    },
                    url = url, // URL of the video
                    playerConfig = playerConfig, // Player configuration
                    isPause = isPause, // Flag indicating if the video is paused
                    onPauseToggle = {
                        playerConfig.pauseCallback?.invoke(isPause.not())
                        isPause = isPause.not()
                    }, // Toggle pause/resume
                    showControls = showControls, // Flag indicating if controls should be shown
                    onShowControlsToggle = {
                        showControls = showControls.not()
                    }, // Toggle show/hide controls
                    onChangeSeekbar = { isSeekbarSliding = it }, // Update seek bar sliding state
                    isFullScreen = isFullScreen,
                    onFullScreenToggle = { isFullScreen = isFullScreen.not() }
                )
            }

        } else {
                // Video player with control
                YoutubePlayerWithControl(
                    modifier = if (isFullScreen) { Modifier.fillMaxSize()} else { modifier },
                    url = videoUrl, // URL of the video
                    playerConfig = playerConfig, // Player configuration
                    isPause = isPause, // Flag indicating if the video is paused
                    onPauseToggle = {
                        playerConfig.pauseCallback?.invoke(isPause.not())
                        isPause = isPause.not()
                    }, // Toggle pause/resume
                    showControls = showControls, // Flag indicating if controls should be shown
                    onShowControlsToggle = { showControls = showControls.not() }, // Toggle show/hide controls
                    onChangeSeekbar = { isSeekbarSliding = it }, // Update seek bar sliding state
                    isFullScreen = isFullScreen,
                    onFullScreenToggle = { isFullScreen = isFullScreen.not()}
                )
        }
    }
}








