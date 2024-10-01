package chaintech.videoplayer.ui.video

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import chaintech.videoplayer.extension.formatMinSec
import chaintech.videoplayer.extension.formattedInterval
import chaintech.videoplayer.model.PlayerConfig
import chaintech.videoplayer.model.PlayerSpeed
import chaintech.videoplayer.model.ScreenResize
import chaintech.videoplayer.model.desktopGradientBGColors
import chaintech.videoplayer.ui.component.AnimatedClickableIcon
import chaintech.videoplayer.ui.component.DesktopSpeedSelection
import chaintech.videoplayer.util.CMPPlayer
import chaintech.videoplayer.util.isLiveStream

@Composable
internal fun DesktopVideoPlayer(
    modifier: Modifier,
    url: String, // URL of the video
    playerConfig: PlayerConfig, // Configuration for the player
    isPause: Boolean, // Flag indicating if the video is paused
    onPauseToggle: (() -> Unit) // Callback for toggling pause/resume
) {
    var totalTime by remember { mutableStateOf(0) } // Total duration of the video
    var currentTime by remember { mutableStateOf(0) } // Current playback time
    var isSliding by remember { mutableStateOf(false) } // Flag indicating if the seek bar is being slid
    var sliderTime: Int? by remember { mutableStateOf(null) } // Time indicated by the seek bar
    var selectedSpeed by remember { mutableStateOf(PlayerSpeed.X1) } // Selected playback speed
    val screenSize by remember { mutableStateOf(ScreenResize.FILL) } // Selected playback speed
    var isBuffering by remember { mutableStateOf(true) }
    var volume by remember { mutableStateOf(100f) }
    var showSpeedSelection by remember { mutableStateOf(false) }

    var sTime = currentTime
    playerConfig.isMute?.let {
        if (it) {
            volume = 0f
        }
    }

    LaunchedEffect(isBuffering) {
        playerConfig.bufferCallback?.invoke(isBuffering)
    }

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopStart)
                .padding(top = playerConfig.controlTopPadding) // Add padding to the top
        ) {
            // Video player component
            CMPPlayer(
                modifier = Modifier.fillMaxWidth().weight(1f),
                url = url,
                isPause = isPause,
                isMute = false,
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
                volume = volume
            )

            if(playerConfig.showDesktopControls) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    if (isLiveStream(url).not() && playerConfig.isSeekBarVisible) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            if (playerConfig.isDurationVisible) {
                                Text(
                                    modifier = Modifier,
                                    text = currentTime.formatMinSec(), // Format the current time to "MM:SS" format
                                    color = playerConfig.durationTextColor,
                                    style = playerConfig.durationTextStyle
                                )
                            }

                            // Slider for seeking through the media
                            Slider(
                                modifier = Modifier.weight(1f).height(25.dp),
                                value = currentTime.toFloat(), // Current value of the slider
                                onValueChange = {
                                    sTime = it.toInt()
                                    isSliding = true
                                    sliderTime = null
                                    currentTime = it.toInt()
                                },
                                valueRange = 0f..totalTime.toFloat(), // Range of the slider
                                onValueChangeFinished = {
                                    isSliding = false
                                    sliderTime = sTime
                                },
                                colors = SliderDefaults.colors(
                                    thumbColor = playerConfig.seekBarThumbColor,
                                    inactiveTrackColor = playerConfig.seekBarInactiveTrackColor,
                                    activeTrackColor = playerConfig.seekBarActiveTrackColor
                                )
                            )

                            if (playerConfig.isDurationVisible) {
                                Text(
                                    modifier = Modifier,
                                    text = totalTime.formatMinSec(), // Format the total time to "MM:SS" format
                                    color = playerConfig.durationTextColor,
                                    style = playerConfig.durationTextStyle
                                )
                            }
                        }
                    }
                    if (isLiveStream(url)) {
                        LiveStreamView(playerConfig)
                    }

                    ControlPanel(
                        url = url,
                        playerConfig = playerConfig,
                        isPause = isPause,
                        isBuffering = isBuffering,
                        currentTime = currentTime,
                        totalTime = totalTime,
                        volume = volume,
                        sliderTimeCallback = { sliderTime = it},
                        onPauseToggle = onPauseToggle,
                        volumeCallback = { volume = it },
                        isSlidingCallback= { isSliding = it},
                        showSpeedSelectionCallback = { showSpeedSelection = showSpeedSelection.not()}
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        Box(modifier = Modifier
            .align(Alignment.BottomEnd)
        ) {
            DesktopSpeedSelectionOverlay(
                selectedSpeed = selectedSpeed,
                selectedSpeedCallback = { selectedSpeed = it},
                showSpeedSelection = showSpeedSelection,
                showSpeedSelectionCallback = { showSpeedSelection = it}
            )
        }
    }
}

@Composable
private fun LiveStreamView(
    playerConfig: PlayerConfig // Configuration object for styling options
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp), // Horizontal padding for the row
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp) // Space between elements
    ) {
        Spacer(modifier = Modifier.weight(1f)) // Spacer to push content to the right

        // Live indicator container
        Row(
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.2f), shape = RoundedCornerShape(3.dp))
                .padding(vertical = 4.dp, horizontal = 8.dp), // Padding for better appearance
            horizontalArrangement = Arrangement.spacedBy(5.dp), // Space between elements
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Live dot indicator
            Box(
                modifier = Modifier
                    .size(10.dp) // Size of the dot
                    .background(Color.Red, shape = CircleShape) // Red color and circular shape
            )

            // Display "Live" text
            Text(
                text = "Live", // Indicate that this is a live stream
                color = playerConfig.durationTextColor,
                style = playerConfig.durationTextStyle,
                modifier = Modifier.padding(end = 6.dp) // Padding at the end for spacing
            )
        }
    }
}

@Composable
private fun ControlPanel(
    url: String,
    playerConfig: PlayerConfig,
    isPause: Boolean,
    isBuffering: Boolean,
    currentTime: Int,
    totalTime: Int,
    volume: Float,
    onPauseToggle: () -> Unit,
    sliderTimeCallback: (Int) -> Unit,
    volumeCallback: (Float) -> Unit,
    isSlidingCallback: (Boolean) -> Unit,
    showSpeedSelectionCallback: () -> Unit
) {
    var lastSavedVolume by remember { mutableStateOf(100f) }
    var changeV by remember { mutableStateOf(100f) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Fast Backward Icon
        if (playerConfig.isFastForwardBackwardEnabled && !isLiveStream(url)) {
            AnimatedClickableIcon(
                painterRes = playerConfig.fastBackwardIconResource,
                imageVector = Icons.Filled.FastRewind,
                contentDescription = "Fast Backward",
                tint = playerConfig.iconsTintColor,
                iconSize = playerConfig.fastForwardBackwardIconSize,
                onClick = {
                    isSlidingCallback(true)
                    val newTime = maxOf(0, currentTime - playerConfig.fastForwardBackwardIntervalSeconds.formattedInterval())
                    sliderTimeCallback(newTime)
                    isSlidingCallback(false)
                }
            )
        }

        // Play/Pause Icon
        if (playerConfig.isPauseResumeEnabled) {
            Box(modifier = Modifier.size(playerConfig.pauseResumeIconSize)) {
                if (isBuffering) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center).fillMaxSize(),
                        color = playerConfig.loadingIndicatorColor
                    )
                }
                AnimatedClickableIcon(
                    painterRes = if (isPause) playerConfig.playIconResource else playerConfig.pauseIconResource,
                    imageVector = if (isPause) Icons.Filled.PlayArrow else Icons.Filled.Pause,
                    contentDescription = "Play/Pause",
                    tint = playerConfig.iconsTintColor,
                    iconSize = playerConfig.pauseResumeIconSize,
                    onClick = { onPauseToggle() }
                )
            }
        }

        // Fast Forward Icon
        if (playerConfig.isFastForwardBackwardEnabled && !isLiveStream(url)) {
            AnimatedClickableIcon(
                painterRes = playerConfig.fastForwardIconResource,
                imageVector = Icons.Filled.FastForward,
                contentDescription = "Fast Forward",
                tint = playerConfig.iconsTintColor,
                iconSize = playerConfig.fastForwardBackwardIconSize,
                onClick = {
                    isSlidingCallback(true)
                    val newTime = minOf(totalTime, currentTime + playerConfig.fastForwardBackwardIntervalSeconds.formattedInterval())
                    sliderTimeCallback(newTime)
                    isSlidingCallback(false)
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Mute/Unmute Icon and Volume Slider
        if (playerConfig.isMuteControlEnabled) {
            AnimatedClickableIcon(
                painterRes = if (volume == 0f) playerConfig.unMuteIconResource else playerConfig.muteIconResource,
                imageVector = if (volume == 0f) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = "Mute/Unmute",
                tint = playerConfig.iconsTintColor,
                iconSize = playerConfig.topControlSize,
                onClick = {
                    volumeCallback(if (volume > 0) 0f else lastSavedVolume)
                }
            )
            Slider(
                modifier = Modifier.width(100.dp).height(25.dp),
                value = volume, // Current value of the slider
                onValueChange = {
                    volumeCallback(it)
                    changeV = it
                },
                valueRange = 0f..100f, // Range of the slider
                onValueChangeFinished = { lastSavedVolume = changeV },
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFFFA500),
                    inactiveTrackColor = Color(0xFFD3D3D3),
                    activeTrackColor = Color(0xFF008080)
                )
            )
        }

        // Speed Control Icon
        if (playerConfig.isSpeedControlEnabled && !isLiveStream(url)) {
            AnimatedClickableIcon(
                painterRes = playerConfig.speedIconResource,
                imageVector = Icons.Default.Speed,
                contentDescription = "Speed",
                tint = playerConfig.iconsTintColor,
                iconSize = playerConfig.topControlSize,
                onClick = { showSpeedSelectionCallback() } // Toggle Speed on click
            )
        }
    }
}

@Composable
private fun DesktopSpeedSelectionOverlay(
    selectedSpeed: PlayerSpeed,
    selectedSpeedCallback: (PlayerSpeed) -> Unit,
    showSpeedSelection: Boolean,
    showSpeedSelectionCallback: (Boolean) -> Unit
) {
    Box {
        // Playback speed options popup background
        AnimatedVisibility(
            visible = showSpeedSelection,
            enter = fadeIn(),
            exit = fadeOut(animationSpec = tween(durationMillis = 700))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = Brush.horizontalGradient(desktopGradientBGColors))
                    .pointerInput(Unit) {
                        detectTapGestures { showSpeedSelectionCallback(false) } // Hide speed selection on background tap
                    }
            )
        }

        // Playback speed options popup
        AnimatedVisibility(
            visible = showSpeedSelection,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth }, // Start from the right edge
                animationSpec = tween(durationMillis = 500) // Animation duration
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth }, // Slide out to the right edge
                animationSpec = tween(durationMillis = 500) // Animation duration
            )
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                DesktopSpeedSelection(
                    buttonSize = 40.dp,
                    selectedSpeed = selectedSpeed,
                    onSelectSpeed = { speed ->
                        speed?.let { selectedSpeedCallback(it) }
                        showSpeedSelectionCallback(false) // Hide speed selection after selecting a speed
                    }
                )
            }
        }
    }
}