package chaintech.videoplayer.ui.audio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.RepeatOneOn
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.ShuffleOn
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import chaintech.videoplayer.extension.formatMinSec
import chaintech.videoplayer.model.AudioFile
import chaintech.videoplayer.model.AudioPlayerConfig
import chaintech.videoplayer.ui.component.AnimatedClickableIcon
import chaintech.videoplayer.util.CMPAudioPlayer
import chaintech.videoplayer.util.ImageFromUrl
import kotlin.random.Random


@Composable
fun AudioPlayerView(
    modifier: Modifier = Modifier, // Modifier for the composable
    audios: List<AudioFile>, // URL of the video
    audioPlayerConfig: AudioPlayerConfig = AudioPlayerConfig(), // Configuration for the player
    currentItemIndex: ((Int) -> Unit)? = null
){
    var isPause by remember { mutableStateOf(false) } // State for pausing/resuming audio
    var totalTime by remember { mutableStateOf(0) } // Total duration of the audio
    var currentTime by remember { mutableStateOf(0) } // Current playback time
    var isSliding by remember { mutableStateOf(false) } // Flag indicating if the seek bar is being slid
    var sliderTime: Int? by remember { mutableStateOf(null) } // Time indicated by the seek bar
    var isLoading by remember { mutableStateOf(true) } // Flag indicating audio buffer
    var currentIndex by remember { mutableStateOf(0) }
    var isShuffle by remember { mutableStateOf(false) } // State for Shuffle
    var isRepeat by remember { mutableStateOf(false) } // State for repeat one

    fun changeAudio(isNext: Boolean) {
        // Function to get a random index for shuffling
        fun getNextShuffleIndex(): Int {
            if (audios.size <= 1) return Random.nextInt(0, audios.size)

            var newIndex: Int
            do {
                newIndex = Random.nextInt(0, audios.size)
            } while (newIndex == currentIndex)

            return newIndex
        }

        // Change audio index based on whether moving to the next or previous audio
        if (isNext) {
            currentIndex = if (isShuffle) {
                getNextShuffleIndex() // Get a random index if shuffling
            } else {
                (currentIndex + 1) % audios.size // Move to the next audio
            }
        } else {
            // Move to the previous audio or reset slider time if at the start
            if (currentIndex > 0) {
                currentIndex -= 1 // Move to previous audio
            } else {
                isSliding = true
                sliderTime = 0 // Reset slider time
                isSliding = false
            }
        }

        // Update player state
        isPause = false // Ensure audio is playing
        isLoading = true // Set loading state while changing audio
    }

    LaunchedEffect(currentIndex) {
        currentItemIndex?.let {
            it(currentIndex)
        }
    }

    // Container for the audio player and control components
    Box(
        modifier = modifier
            .background(audioPlayerConfig.backgroundColor)
    ) {
        // Check if there are any audios to play
        if (audios.isNotEmpty()) {
            // Audio player component
            CMPAudioPlayer(
                modifier = modifier,
                url = audios[currentIndex].audioUrl,
                isPause = isPause,
                totalTime = { totalTime = it }, // Update total time of the audio
                currentTime = {
                    if (!isSliding) {
                        currentTime = it // Update current playback time
                        sliderTime = null // Reset slider time if not sliding
                    }
                },
                isSliding = isSliding, // Pass seek bar sliding state
                sliderTime = sliderTime, // Pass seek bar slider time
                isRepeat = isRepeat,
                loadingState = { isLoading = it },
                didEndAudio = { changeAudio(true) }
            )
        }

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart) // Align the column to the bottom
                    .padding(bottom = audioPlayerConfig.controlsBottomPadding),
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f)
                        .padding(horizontal = 25.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.weight(0.25f))

                    // Display album art
                    AlbumArt(
                        audioPlayerConfig = audioPlayerConfig,
                        thumbnailUrl = audios[currentIndex].thumbnailUrl
                    )

                    // Display audio title if it's not empty
                    if (audios[currentIndex].audioTitle.isNotEmpty()) {
                        Text(
                            modifier = Modifier.padding(top = 25.dp),
                            text = audios[currentIndex].audioTitle,
                            color = audioPlayerConfig.fontColor,
                            style = audioPlayerConfig.titleTextStyle,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.weight(0.05f))
                }

                // Show controls if they are visible in configuration
                if (audioPlayerConfig.isControlsVisible) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
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
                                    currentTime = it.toInt()
                                    isSliding = true
                                    sliderTime = null
                                },
                                valueRange = 0f..totalTime.toFloat(), // Range of the slider
                                onValueChangeFinished = {
                                    isSliding = false
                                    sliderTime = currentTime
                                },
                                colors = SliderDefaults.colors(
                                    thumbColor = audioPlayerConfig.seekBarThumbColor,
                                    inactiveTrackColor = audioPlayerConfig.seekBarInactiveTrackColor,
                                    activeTrackColor = audioPlayerConfig.seekBarActiveTrackColor
                                )
                            )

                            // Display time details (current and total time)
                            TimeDetails(
                                audioPlayerConfig = audioPlayerConfig,
                                currentTime = currentTime,
                                totalTime = totalTime
                            )
                        }
                    }

                    // Control panel for playback controls (play, pause, next, previous, shuffle, repeat)
                    ControlPanel(
                        audioPlayerConfig = audioPlayerConfig,
                        isPause = isPause,
                        isRepeat = isRepeat,
                        isLoading = isLoading,
                        isShuffle = isShuffle,
                        onPlayPauseClick = { isPause = !isPause },
                        onNextClick = { changeAudio(isNext = true) },
                        onPreviousClick = { changeAudio(isNext = false) },
                        onShuffleClick = { isShuffle = !isShuffle },
                        onRepeatClick = { isRepeat = !isRepeat }
                    )
                }
            }
        }
    }
}

@Composable
fun AlbumArt(
    audioPlayerConfig: AudioPlayerConfig,
    thumbnailUrl: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f)
            .clip(RoundedCornerShape(10.dp))
            .background(color = audioPlayerConfig.coverBackground, shape = RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Display music note icon as a fallback
        Icon(
            imageVector = Icons.Default.MusicNote,
            contentDescription = "Music Note",
            tint = Color.White,
            modifier = Modifier.fillMaxSize(0.8f) // Scale down the icon size
        )

        // Load the album thumbnail if the URL is not empty
        if (thumbnailUrl.isNotEmpty()) {
            ImageFromUrl(
                modifier = Modifier.fillMaxSize(),
                data = thumbnailUrl,
                contentScale = ContentScale.Crop // Crop the image to fill the box
            )
        }
    }
}

@Composable
fun TimeDetails(
    audioPlayerConfig: AudioPlayerConfig,
    currentTime: Int,
    totalTime: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp), // Add padding to the top
        horizontalArrangement = Arrangement.SpaceBetween // Distribute space evenly between child components
    ) {
        // Display the current playback time
        Text(
            text = currentTime.formatMinSec(), // Format the current time to "MM:SS" format
            color = audioPlayerConfig.fontColor,
            style = audioPlayerConfig.durationTextStyle
        )

        Spacer(modifier = Modifier.weight(1f)) // Add a spacer to push the total time to the right

        // Display the total duration of the media
        Text(
            text = totalTime.formatMinSec(), // Format the total time to "MM:SS" format
            color = audioPlayerConfig.fontColor,
            style = audioPlayerConfig.durationTextStyle
        )
    }
}

@Composable
fun ControlPanel(
    audioPlayerConfig: AudioPlayerConfig,
    isPause: Boolean,
    isRepeat: Boolean,
    isLoading: Boolean,
    isShuffle: Boolean,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onShuffleClick: () -> Unit,
    onRepeatClick: () -> Unit
) {
    // Define the height of the control panel based on icon sizes
    val controlPanelHeight = max(
        audioPlayerConfig.pauseResumeIconSize,
        audioPlayerConfig.previousNextIconSize
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(controlPanelHeight),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        // Repeat Icon
        AnimatedClickableIcon(
            painterRes = if (isRepeat) audioPlayerConfig.repeatOnIconResource else audioPlayerConfig.repeatOffIconResource,
            imageVector = if (isRepeat) Icons.Filled.RepeatOneOn else Icons.Filled.RepeatOne,
            contentDescription = "Repeat",
            tint = audioPlayerConfig.iconsTintColor,
            iconSize = audioPlayerConfig.advanceControlIconSize,
            onClick = {
                onRepeatClick()
            }
        )

        // Previous Icon
        AnimatedClickableIcon(
            painterRes = audioPlayerConfig.previousIconResource,
            imageVector = Icons.Filled.SkipPrevious,
            contentDescription = "Previous",
            tint = audioPlayerConfig.iconsTintColor,
            iconSize = audioPlayerConfig.previousNextIconSize,
            onClick = {
                onPreviousClick()
            }
        )

        // Play/Pause Icon with Loading Indicator
        Box {
            AnimatedClickableIcon(
                painterRes = if (isPause) audioPlayerConfig.playIconResource else audioPlayerConfig.pauseIconResource,
                imageVector = if (isPause) Icons.Filled.PlayArrow else Icons.Filled.Pause,
                contentDescription = "Play/Pause",
                tint = audioPlayerConfig.iconsTintColor,
                iconSize = audioPlayerConfig.pauseResumeIconSize,
                onClick = { onPlayPauseClick() }
            )

            if (isLoading) {
                Box(modifier = Modifier.size(audioPlayerConfig.pauseResumeIconSize)) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center).fillMaxSize(),
                        color = audioPlayerConfig.loadingIndicatorColor
                    )
                }
            }
        }

        // Next Icon
        AnimatedClickableIcon(
            painterRes = audioPlayerConfig.nextIconResource,
            imageVector = Icons.Filled.SkipNext,
            contentDescription = "Next",
            tint = audioPlayerConfig.iconsTintColor,
            iconSize = audioPlayerConfig.previousNextIconSize,
            onClick = {
                onNextClick()
            }
        )

        // Shuffle Icon
        AnimatedClickableIcon(
            painterRes = if (isShuffle) audioPlayerConfig.shuffleOnIconResource else audioPlayerConfig.shuffleOffIconResource,
            imageVector = if (isShuffle) Icons.Filled.ShuffleOn else Icons.Filled.Shuffle,
            contentDescription = "Shuffle",
            tint = audioPlayerConfig.iconsTintColor,
            iconSize = audioPlayerConfig.advanceControlIconSize,
            onClick = { onShuffleClick() }
        )
    }
}