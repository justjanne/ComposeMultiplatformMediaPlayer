package chaintech.videoplayer.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import chaintech.videoplayer.model.PlayerSpeed
import chaintech.videoplayer.model.ScreenResize

@Composable
internal actual fun CMPPlayer(
    modifier: Modifier,
    url: String,
    isPause: Boolean,
    isMute: Boolean,
    totalTime: (Int) -> Unit,
    currentTime: (Int) -> Unit,
    isSliding: Boolean,
    sliderTime: Int?,
    speed: PlayerSpeed,
    size: ScreenResize,
    bufferCallback: (Boolean) -> Unit,
    didEndVideo: () -> Unit,
    loop: Boolean,
    volume: Float
) {
    val mediaPlayerComponent = remember { initializeMediaPlayerComponent() }
    val mediaPlayer = remember { mediaPlayerComponent.mediaPlayer() }
    var time by remember { mutableStateOf(0) }
    mediaPlayer.setupVideoListeners(
        onFinish = {
            currentTime(time)
            mediaPlayer.submit { mediaPlayer.controls().play() }
            didEndVideo()
        },
        onBuffer = { bufferCallback(it) },
        totalTime = {
            time = it
            totalTime(it)
        },
        currentTime = { currentTime(it) },
    )

    val factory = remember { { mediaPlayerComponent }}

    LaunchedEffect(url) { mediaPlayer.media().play(url) }
    LaunchedEffect(speed) {
        mediaPlayer.controls().setRate(
        when (speed) {
        PlayerSpeed.X0_5 -> 0.5f
        PlayerSpeed.X1 -> 1f
        PlayerSpeed.X1_5 -> 1.5f
        PlayerSpeed.X2 -> 2f
    } )
    }
    LaunchedEffect(volume) {
        mediaPlayer.audio().setVolume(volume.toInt())
    }
    LaunchedEffect(isPause) { mediaPlayer.controls().setPause(isPause) }

    LaunchedEffect(sliderTime) {
        sliderTime?.let {
            val per = (it * 100 / time).toFloat() / 100
            mediaPlayer.controls().setPosition(per)
        }
    }
    SwingPanel(
        background = Color.Transparent,
        modifier = modifier,
        factory = factory
    )

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }
}