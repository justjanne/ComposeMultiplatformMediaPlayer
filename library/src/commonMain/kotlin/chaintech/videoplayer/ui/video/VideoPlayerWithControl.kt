package chaintech.videoplayer.ui.video

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import chaintech.videoplayer.extension.formattedInterval
import chaintech.videoplayer.model.PlayerConfig
import chaintech.videoplayer.model.PlayerSpeed
import chaintech.videoplayer.model.ScreenResize
import chaintech.videoplayer.ui.component.LiveStreamView
import chaintech.videoplayer.ui.video.controls.BottomControlView
import chaintech.videoplayer.ui.video.controls.CenterControlView
import chaintech.videoplayer.ui.video.controls.LockScreenView
import chaintech.videoplayer.ui.video.controls.SpeedSelectionOverlay
import chaintech.videoplayer.ui.video.controls.TopControlView
import chaintech.videoplayer.util.CMPPlayer
import chaintech.videoplayer.util.isLiveStream

@Composable
internal fun VideoPlayerWithControl(
    modifier: Modifier,
    url: String, // URL of the video
    playerConfig: PlayerConfig, // Configuration for the player
    isPause: Boolean, // Flag indicating if the video is paused
    onPauseToggle: (() -> Unit), // Callback for toggling pause/resume
    showControls: Boolean, // Flag indicating if controls should be shown
    onShowControlsToggle: (() -> Unit), // Callback for toggling show/hide controls
    onChangeSeekbar: ((Boolean) -> Unit), // Callback for seek bar sliding
    isFullScreen: Boolean,
    onFullScreenToggle: (() -> Unit)
) {
    var totalTime by remember { mutableStateOf(0) } // Total duration of the video
    var currentTime by remember { mutableStateOf(0) } // Current playback time
    var isSliding by remember { mutableStateOf(false) } // Flag indicating if the seek bar is being slid
    var sliderTime: Int? by remember { mutableStateOf(null) } // Time indicated by the seek bar
    var isMute by remember { mutableStateOf(false) } // Flag indicating if the audio is muted
    var selectedSpeed by remember { mutableStateOf(PlayerSpeed.X1) } // Selected playback speed
    var showSpeedSelection by remember { mutableStateOf(false) } // Selected playback speed
    var isScreenLocked by remember { mutableStateOf(false) }
    var screenSize by remember { mutableStateOf(ScreenResize.FILL) } // Selected playback speed
    var isBuffering by remember { mutableStateOf(true) }

    playerConfig.isMute?.let {
        isMute = it
    }

    LaunchedEffect(isBuffering) {
        playerConfig.bufferCallback?.invoke(isBuffering)
    }

    // Container for the video player and control components
    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { _ ->
                    onShowControlsToggle() // Toggle show/hide controls on tap
                    showSpeedSelection = false
                }
            }
    ) {
        // Video player component
        CMPPlayer(
            modifier = modifier,
            url = url,
            isPause = isPause,
            isMute = isMute,
            totalTime = { totalTime = it }, // Update total time of the video
            currentTime = {
                if (isSliding.not()) {
                    currentTime = it // Update current playback time
                    sliderTime = null // Reset slider time if not sliding
                }
            },
            isSliding = isSliding, // Pass seek bar sliding state
            sliderTime = sliderTime, // Pass seek bar slider time
            speed = selectedSpeed, // Pass selected playback speed
            size = screenSize,
            bufferCallback = { isBuffering = it },
            didEndVideo = {
                playerConfig.didEndVideo?.invoke()
                if(playerConfig.loop.not()){
                    onPauseToggle()
                }
            },
            loop = playerConfig.loop,
            volume = 0f
        )

        if (isScreenLocked.not()) {
            // Top control view for playback speed and mute/unMute
            TopControlView(
                playerConfig = playerConfig,
                isMute = isMute,
                onMuteToggle = {
                    playerConfig.muteCallback?.invoke(isMute.not())
                    isMute = isMute.not()
                }, // Toggle mute/unMute
                showControls = showControls, // Pass show/hide controls state
                onTapSpeed = { showSpeedSelection = showSpeedSelection.not() },
                isFullScreen = isFullScreen,
                onFullScreenToggle = { onFullScreenToggle() },
                onLockScreenToggle = { isScreenLocked = isScreenLocked.not()},
                onResizeScreenToggle = {
                    screenSize = when(screenSize) {
                        ScreenResize.FIT -> ScreenResize.FILL
                        ScreenResize.FILL -> ScreenResize.FIT
                    }
                },
                isLiveStream = isLiveStream(url),
                selectedSize = screenSize
            )

            // Center control view for pause/resume and fast forward/backward actions
            CenterControlView(
                playerConfig = playerConfig,
                isPause = isPause,
                onPauseToggle = onPauseToggle,
                onBackwardToggle = { // Seek backward
                    isSliding = true
                    val newTime =
                        currentTime - playerConfig.fastForwardBackwardIntervalSeconds.formattedInterval()
                    sliderTime = if (newTime < 0) {
                        0
                    } else {
                        newTime
                    }
                    isSliding = false
                },
                onForwardToggle = { // Seek forward
                    isSliding = true
                    val newTime =
                        currentTime + playerConfig.fastForwardBackwardIntervalSeconds.formattedInterval()
                    sliderTime = if (newTime > totalTime) {
                        totalTime
                    } else {
                        newTime
                    }
                    isSliding = false
                },
                showControls = showControls,
                isLiveStream = isLiveStream(url)
            )

            if (isLiveStream(url)) {
                LiveStreamView(playerConfig)
            } else {
                // Bottom control view for seek bar and time duration display
                BottomControlView(
                    playerConfig = playerConfig,
                    currentTime = currentTime, // Pass current playback time
                    totalTime = totalTime, // Pass total duration of the video
                    showControls = showControls, // Pass show/hide controls state
                    onChangeSliderTime = { sliderTime = it }, // Update seek bar slider time
                    onChangeCurrentTime = { currentTime = it }, // Update current playback time
                    onChangeSliding = { // Update seek bar sliding state
                        isSliding = it
                        onChangeSeekbar(isSliding)
                    }
                )
            }
        } else {
            if (playerConfig.isScreenLockEnabled) {
                LockScreenView(
                    playerConfig = playerConfig,
                    showControls = showControls,
                    onLockScreenToggle = { isScreenLocked = isScreenLocked.not()}
                )
            }
        }
        if (isBuffering) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if(playerConfig.loaderView != null) {
                    playerConfig.loaderView?.invoke()
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center).size(playerConfig.pauseResumeIconSize),
                        color = playerConfig.loadingIndicatorColor
                    )
                }
            }
        }

        SpeedSelectionOverlay(
            playerConfig = playerConfig,
            selectedSpeed = selectedSpeed,
            selectedSpeedCallback = { selectedSpeed = it },
            showSpeedSelection = showSpeedSelection,
            showSpeedSelectionCallback = { showSpeedSelection = it}
        )
    }
}

