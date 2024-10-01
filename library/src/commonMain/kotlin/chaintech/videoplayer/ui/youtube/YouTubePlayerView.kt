package chaintech.videoplayer.ui.youtube

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import chaintech.videoplayer.model.PlayerConfig
import chaintech.videoplayer.util.LandscapeOrientation
import chaintech.videoplayer.util.extractYouTubeVideoId
import kotlinx.coroutines.delay

@Composable
fun YouTubePlayerView(
    modifier: Modifier = Modifier, // Modifier for the composable
    videoId: String,
    playerConfig: PlayerConfig = PlayerConfig() // Configuration for the player
) {
    var isPause by remember { mutableStateOf(false) } // State for pausing/resuming video
    var showControls by remember { mutableStateOf(true) } // State for showing/hiding controls
    var isSeekbarSliding = false // Flag for indicating if the seek bar is being slid
    var isFullScreen by remember { mutableStateOf(false) }

    playerConfig.isPause?.let {
        isPause = it
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
    val videoUrl = extractYouTubeVideoId(videoId) ?: videoId

    LandscapeOrientation(isFullScreen) {
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