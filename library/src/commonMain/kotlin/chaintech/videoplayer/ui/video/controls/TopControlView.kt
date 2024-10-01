package chaintech.videoplayer.ui.video.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Speed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import chaintech.videoplayer.model.PlayerConfig
import chaintech.videoplayer.model.ScreenResize
import chaintech.videoplayer.ui.component.AnimatedClickableIcon
import reelsdemo.composemultiplatformmediaplayer.generated.resources.Res
import reelsdemo.composemultiplatformmediaplayer.generated.resources.resize_fill
import reelsdemo.composemultiplatformmediaplayer.generated.resources.resize_fit


@Composable
internal fun TopControlView(
    playerConfig: PlayerConfig, // Configuration object for the player, includes styling options
    isMute: Boolean, // Flag indicating whether the audio is muted
    onMuteToggle: (() -> Unit), // Callback for toggling mute/unMute
    showControls: Boolean, // Flag indicating whether controls should be shown
    onTapSpeed: (() -> Unit), // Callback for changing playback speed
    isFullScreen: Boolean,
    onFullScreenToggle: (() -> Unit),
    onLockScreenToggle: (() -> Unit),
    onResizeScreenToggle: (() -> Unit),
    isLiveStream: Boolean = false,
    selectedSize: ScreenResize? = null
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopStart)
                .padding(top = playerConfig.controlTopPadding) // Add padding to the top
        ) {
            // Show controls with animation based on the visibility flag
            AnimatedVisibility(
                modifier = Modifier,
                visible = showControls,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement =  Arrangement.spacedBy(15.dp, alignment = Alignment.End) // Spacing between items with end alignment
                ) {
                    if (playerConfig.isScreenLockEnabled) {
                        AnimatedClickableIcon(
                            imageVector = Icons.Default.LockOpen ,
                            contentDescription = "Lock",
                            tint = playerConfig.iconsTintColor,
                            iconSize = playerConfig.topControlSize,
                            onClick = { onLockScreenToggle() } // Toggle Lock on click
                        )
                    }
                    // If speed control is enabled, show the speed control button
                    if (playerConfig.isSpeedControlEnabled && isLiveStream.not()) {
                        AnimatedClickableIcon(
                            painterRes = playerConfig.speedIconResource,
                            imageVector = Icons.Default.Speed,
                            contentDescription = "Speed",
                            tint = playerConfig.iconsTintColor,
                            iconSize = playerConfig.topControlSize,
                            onClick = { onTapSpeed() } // Toggle Speed on click
                        )
                    }

                    // If mute control is enabled, show the mute/unMute button
                    if (playerConfig.isMuteControlEnabled) {
                        AnimatedClickableIcon(
                            painterRes = if (isMute) playerConfig.unMuteIconResource else playerConfig.muteIconResource,
                            imageVector = if (isMute) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Mute/UnMute",
                            tint = playerConfig.iconsTintColor,
                            iconSize = playerConfig.topControlSize,
                            onClick = { onMuteToggle() } // Toggle mute/unMute on click
                        )
                    }

                    if(playerConfig.isScreenResizeEnabled) {
                        selectedSize?.let {
                            AnimatedClickableIcon(
                                painterRes = when(selectedSize) {
                                    ScreenResize.FIT -> Res.drawable.resize_fit
                                    ScreenResize.FILL -> Res.drawable.resize_fill
                                },
                                contentDescription = "Resize",
                                tint = playerConfig.iconsTintColor,
                                iconSize = playerConfig.topControlSize,
                                colorFilter = ColorFilter.tint(playerConfig.iconsTintColor),
                                onClick = { onResizeScreenToggle() }
                            )
                        }
                    }

                    if (playerConfig.isFullScreenEnabled) {
                        AnimatedClickableIcon(
                            imageVector = if (isFullScreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                            contentDescription = "Mute/UnMute",
                            tint = playerConfig.iconsTintColor,
                            iconSize = playerConfig.topControlSize,
                            onClick = { onFullScreenToggle() } // Toggle mute/unMute on click
                        )
                    }
                }
            }
        }
    }
}


