package chaintech.videoplayer.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
internal actual fun CMPAudioPlayer(
    modifier: Modifier,
    url: String,
    isPause: Boolean,
    totalTime: (Int) -> Unit,
    currentTime: (Int) -> Unit,
    isSliding: Boolean,
    sliderTime: Int?,
    isRepeat: Boolean,
    loadingState: (Boolean) -> Unit,
    didEndAudio: () -> Unit
) {
    var repeatStatus by remember { mutableStateOf(isRepeat) }
    val mediaPlayerComponent = remember { initializeMediaPlayerComponent() }
    val mediaPlayer = remember { mediaPlayerComponent.mediaPlayer() }
    var time by remember { mutableStateOf(0) }

    mediaPlayer.setupVideoListeners(
        onFinish = {
            if (repeatStatus) {
                mediaPlayer.submit { mediaPlayer.controls().play() }
            } else {
                didEndAudio()
            }
        },
        onBuffer = { loadingState(it) },
        totalTime = {
            time = it
            totalTime(it)
        },
        currentTime = { currentTime(it) },
    )

    // Update repeat status when it changes
    LaunchedEffect(isRepeat) {
        repeatStatus = isRepeat
    }
    LaunchedEffect(sliderTime) {
         sliderTime?.let {
             val per = (it * 100 / time).toFloat() / 100
             mediaPlayer.controls().setPosition(per)
        }
    }
    LaunchedEffect(url) { mediaPlayer.media().play(url) }

    LaunchedEffect(isPause) { mediaPlayer.controls().setPause(isPause) }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }
}