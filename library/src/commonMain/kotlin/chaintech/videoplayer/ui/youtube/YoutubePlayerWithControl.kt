package chaintech.videoplayer.ui.youtube

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import chaintech.videoplayer.model.PlayerConfig
import chaintech.videoplayer.model.PlayerSpeed
import chaintech.videoplayer.model.gradientBGColors
import chaintech.videoplayer.ui.component.SpeedSelectionView
import chaintech.videoplayer.ui.video.controls.BottomControlView
import chaintech.videoplayer.ui.video.controls.CenterControlView
import chaintech.videoplayer.ui.video.controls.LockScreenView
import chaintech.videoplayer.ui.video.controls.SpeedSelectionOverlay
import chaintech.videoplayer.ui.video.controls.TopControlView
import chaintech.videoplayer.util.rememberAppBackgroundObserver
import chaintech.videoplayer.util.youtubeProgressColor
import chaintech.videoplayer.youtube.YoutubeHost
import chaintech.videoplayer.youtube.YoutubePlayer
import chaintech.videoplayer.youtube.YoutubePlayerState
import chaintech.videoplayer.youtube.YoutubeEvent
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@Composable
internal fun YoutubePlayerWithControl(
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
    var isMute by remember { mutableStateOf(false) } // Flag indicating if the audio is muted
    var selectedSpeed by remember { mutableStateOf(PlayerSpeed.X1) } // Selected playback speed
    var showSpeedSelection by remember { mutableStateOf(false) } // Selected playback speed
    var isScreenLocked by remember { mutableStateOf(false) }
    var isInitializing by remember { mutableStateOf(true) }
    var pause by remember {  mutableStateOf(isPause) }

    pause = isPause
    //Update mute status with configuration
    playerConfig.isMute?.let {
        isMute = it
    }
    playerConfig.isScreenResizeEnabled = false

    //Handle app lifecycle
    val appBackgroundObserver = rememberAppBackgroundObserver()
    LaunchedEffect(Unit) {
        appBackgroundObserver.observe {
            if (pause.not()) {
                onPauseToggle()
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            appBackgroundObserver.removeObserver()
        }
    }

    val coroutineScope = rememberCoroutineScope()
    val hostState = remember { YoutubeHost() }

    // React to changes in the url parameter
    LaunchedEffect(url) {
        coroutineScope.launch {
            hostState.loadVideo(url)
        }
    }
    LaunchedEffect(hostState.isBuffer) {
        playerConfig.bufferCallback?.invoke(hostState.isBuffer)
    }

    when(val state = hostState.state) {
        is YoutubePlayerState.ErrorState -> { isInitializing = false }
        YoutubePlayerState.Idle -> {
            isInitializing = true
        }
        is YoutubePlayerState.PlayingState -> {
            totalTime = state.totalDuration.inWholeSeconds.toInt()
            if (isSliding.not()) {
                currentTime = state.currentTime.inWholeSeconds.toInt() // Update current playback time
            }
            if(state.playbackStatus == YoutubeEvent.StatusUpdated.PlaybackStatus.FINISHED) {
                playerConfig.didEndVideo?.invoke()
                coroutineScope.launch {
                    hostState.seekTo(0.seconds)
                    hostState.play()
                }
            }
            isInitializing = false
        }
        YoutubePlayerState.Initialized -> coroutineScope.launch {
            hostState.loadVideo(url)
        }
    }
    LaunchedEffect(isPause) {
        coroutineScope.launch {
            if (hostState.state is YoutubePlayerState.PlayingState) {
                if (isPause) {
                    hostState.pause()
                } else {
                    hostState.play()
                }
            }
        }
    }
    LaunchedEffect(isMute) {
        coroutineScope.launch {
            if (hostState.state is YoutubePlayerState.PlayingState) {
                if (isMute) {
                    hostState.mute()
                } else {
                    hostState.unmute()
                }
            }
        }
    }
    LaunchedEffect(selectedSpeed) {
        coroutineScope.launch {
            if (hostState.state is YoutubePlayerState.PlayingState) {
                hostState.setPlaybackRate(
                    when (selectedSpeed) {
                        PlayerSpeed.X0_5 -> 0.5f
                        PlayerSpeed.X1 -> 1f
                        PlayerSpeed.X1_5 -> 1.5f
                        PlayerSpeed.X2 -> 2f
                    }
                )
            }
        }
    }


    // Container for the video player and control components
    Box(
        modifier = modifier
    ) {
        // Video player component
        YoutubePlayer(
            modifier = modifier,
            host = hostState,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isInitializing){ Color.Black
                } else{ Color.Transparent})
                .pointerInput(Unit) {
                    detectTapGestures { _ ->
                        onShowControlsToggle() // Toggle show/hide controls on tap
                        showSpeedSelection = false
                    }
                }
                .wrapContentSize(align = Alignment.Center)
        ) {
            if (isInitializing) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(80.dp)
                        .scale(1.5f)
                        .wrapContentSize(align = Alignment.Center), // Center the progress indicator
                    color = youtubeProgressColor
                )
            }
        }

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
                onResizeScreenToggle = { }
            )

            // Center control view for pause/resume and fast forward/backward actions
            CenterControlView(
                playerConfig = playerConfig,
                isPause = isPause,
                onPauseToggle = onPauseToggle,
                onBackwardToggle = { // Seek backward
                    coroutineScope.launch {
                        hostState.seekBy((-playerConfig.fastForwardBackwardIntervalSeconds).seconds)
                    }
                },
                onForwardToggle = { // Seek forward
                    coroutineScope.launch {
                        hostState.seekBy(playerConfig.fastForwardBackwardIntervalSeconds.seconds)
                    }
                },
                showControls = showControls
            )

            // Bottom control view for seek bar and time duration display
            BottomControlView(
                playerConfig = playerConfig,
                currentTime = currentTime, // Pass current playback time
                totalTime = totalTime, // Pass total duration of the video
                showControls = showControls, // Pass show/hide controls state
                onChangeSliderTime = {
                    it?.let {
                        coroutineScope.launch {
                            hostState.seekTo(it.seconds)
                        }
                    }
                }, // Update seek bar slider time
                onChangeCurrentTime = { currentTime = it }, // Update current playback time
                onChangeSliding = { // Update seek bar sliding state
                    isSliding = it
                    onChangeSeekbar(isSliding)
                },
                isYoutube = true
            )

        } else {
            if (playerConfig.isScreenLockEnabled) {
                LockScreenView(
                    playerConfig = playerConfig,
                    showControls = showControls,
                    onLockScreenToggle = { isScreenLocked = isScreenLocked.not()}
                )
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