package chaintech.videoplayer.ui.reel

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import chaintech.videoplayer.model.PlayerConfig
import chaintech.videoplayer.ui.video.VideoPlayerWithControl
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun ReelsPlayerView(
    modifier: Modifier = Modifier, // Modifier for the composable
    urls: List<String>, // List of video URLs
    playerConfig: PlayerConfig = PlayerConfig() // Configuration for the player
) {
    // Remember the state of the pager
    val pagerState = rememberPagerState(pageCount = {
        urls.size // Set the page count based on the number of URLs
    })

    // Animate scrolling to the current page when it changes
    LaunchedEffect(key1 = pagerState) {
        snapshotFlow { pagerState.currentPage }.distinctUntilChanged().collect { page ->
            pagerState.animateScrollToPage(page)
        }
    }

    var showControls by remember { mutableStateOf(true) } // State for showing/hiding controls
    var isSeekbarSliding = false // Flag for indicating if the seek bar is being slid
    var isFullScreen by remember { mutableStateOf(false) }

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

    // Render vertical pager if enabled, otherwise render horizontal pager
    if (playerConfig.reelVerticalScrolling) {
        VerticalPager(
            state = pagerState,
        ) { page ->
            var isPause by remember { mutableStateOf(false) } // State for pausing/resuming video
            // Video player with control
            VideoPlayerWithControl(
                modifier = modifier,
                url = urls[page], // URL of the video
                playerConfig = playerConfig,
                isPause = if (pagerState.currentPage == page) { isPause } else { true }, // Pause video when not in focus
                onPauseToggle = { isPause = isPause.not() }, // Toggle pause/resume
                showControls = showControls, // Show/hide controls
                onShowControlsToggle = { showControls = showControls.not() }, // Toggle show/hide controls
                onChangeSeekbar = { isSeekbarSliding = it }, // Update seek bar sliding state
                isFullScreen = isFullScreen,
                onFullScreenToggle = { isFullScreen = isFullScreen.not()}
            )
        }
    } else {
        HorizontalPager(
            state = pagerState
        ) { page ->
            var isPause by remember { mutableStateOf(false) } // State for pausing/resuming video
            // Video player with control
            VideoPlayerWithControl(
                modifier = modifier,
                url = urls[page], // URL of the video
                playerConfig = playerConfig,
                isPause = if (pagerState.currentPage == page) { isPause } else { true }, // Pause video when not in focus
                onPauseToggle = { isPause = isPause.not() }, // Toggle pause/resume
                showControls = showControls, // Show/hide controls
                onShowControlsToggle = { showControls = showControls.not() }, // Toggle show/hide controls
                onChangeSeekbar = { isSeekbarSliding = it }, // Update seek bar sliding state
                isFullScreen = isFullScreen,
                onFullScreenToggle = { isFullScreen = isFullScreen.not()}
            )
        }
    }
}

