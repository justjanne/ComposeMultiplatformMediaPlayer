package chaintech.videoplayer.ui.video.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chaintech.videoplayer.model.PlayerConfig
import chaintech.videoplayer.ui.component.TimeDurationView
import chaintech.videoplayer.util.formatYoutubeInterval

@Composable
internal fun BottomControlView(
    playerConfig: PlayerConfig, // Configuration object for the player, includes styling options
    currentTime: Int, // Current playback time in seconds
    totalTime: Int, // Total duration of the media in seconds
    showControls: Boolean, // Flag to determine if controls should be visible
    onChangeSliderTime: ((Int?) -> Unit), // Callback for slider value change
    onChangeCurrentTime: ((Int) -> Unit), // Callback for current time change
    onChangeSliding: ((Boolean) -> Unit), // Callback for slider sliding state change
    isYoutube: Boolean = false
){
    var slideTime = currentTime
    // Only display the seek bar if specified in the player configuration
    if (playerConfig.isSeekBarVisible) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.align(Alignment.BottomStart) // Align the column to the bottom
                    .padding(bottom = playerConfig.seekBarBottomPadding)
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
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(
                            modifier = Modifier.weight(1f) // Occupy remaining horizontal space
                        ) {
                            // Slider for seeking through the media
                            Slider(
                                modifier = Modifier.fillMaxWidth().height(25.dp),
                                value = currentTime.toFloat(), // Current value of the slider
                                onValueChange = {
                                    slideTime = it.toInt()
                                    onChangeSliding(true) // Indicate sliding state
                                    onChangeSliderTime(null) // Reset slider time change callback
                                    onChangeCurrentTime(it.toInt()) // Update current time
                                },
                                valueRange = 0f..totalTime.toFloat(), // Range of the slider
                                onValueChangeFinished = {
                                    onChangeSliding(false) // Indicate sliding state finished
                                    onChangeSliderTime(slideTime) // Update slider time change callback
                                },
                                colors = SliderDefaults.colors(
                                    thumbColor = playerConfig.seekBarThumbColor,
                                    inactiveTrackColor = playerConfig.seekBarInactiveTrackColor,
                                    activeTrackColor = playerConfig.seekBarActiveTrackColor
                                )
                            )

                            // Display current and total duration if specified in player configuration
                            if (playerConfig.isDurationVisible) {
                                TimeDurationView(
                                    playerConfig = playerConfig,
                                    currentTime = if(isYoutube){ formatYoutubeInterval(currentTime)
                                    } else {
                                        currentTime
                                    },
                                    totalTime = if(isYoutube){
                                        formatYoutubeInterval(totalTime)
                                    } else {
                                        totalTime
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
