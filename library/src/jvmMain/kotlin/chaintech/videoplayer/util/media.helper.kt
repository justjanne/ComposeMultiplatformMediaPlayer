package chaintech.videoplayer.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import uk.co.caprica.vlcj.factory.discovery.NativeDiscovery
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter
import uk.co.caprica.vlcj.player.component.CallbackMediaPlayerComponent
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent
import java.awt.Component
import java.util.Locale

@Suppress("UnsafeDynamicallyLoadedCode")
internal fun initializeMediaPlayerComponent(): Component {
    NativeDiscovery().discover()

    return if (isMacOS()) {
        CallbackMediaPlayerComponent()
    } else {
        EmbeddedMediaPlayerComponent()
    }
}

@Composable
internal fun MediaPlayer.setupVideoListeners(
    onFinish: (() -> Unit),
    onBuffer:((Boolean) -> Unit),
    totalTime: (Int) -> Unit,
    currentTime: (Int) -> Unit
) {
    DisposableEffect(Unit) {
        val listener = object : MediaPlayerEventAdapter() {
            override fun finished(mediaPlayer: MediaPlayer) {
                onFinish.invoke()
            }
            override fun buffering(mediaPlayer: MediaPlayer?, newCache: Float) {
                onBuffer(newCache != 100f)
                super.buffering(mediaPlayer, newCache)
            }
            override fun lengthChanged(mediaPlayer: MediaPlayer?, newLength: Long) {
                totalTime((newLength / 1000L).toInt())
                super.lengthChanged(mediaPlayer, newLength)
            }
            override fun timeChanged(mediaPlayer: MediaPlayer?, newTime: Long) {
                currentTime((newTime / 1000L).toInt())
                super.timeChanged(mediaPlayer, newTime)
            }
        }
        events().addMediaPlayerEventListener(listener)
        onDispose { events().removeMediaPlayerEventListener(listener) }
    }
}
/**
 * Returns [MediaPlayer] from player components.
 * The method names are the same, but they don't share the same parent/interface.
 * That's why we need this method.
 */
internal fun Component.mediaPlayer() = when (this) {
    is CallbackMediaPlayerComponent -> mediaPlayer()
    is EmbeddedMediaPlayerComponent -> mediaPlayer()
    else -> error("mediaPlayer() can only be called on vlcj player components")
}

internal fun isMacOS(): Boolean {
    val os = System
        .getProperty("os.name", "generic")
        .lowercase(Locale.ENGLISH)
    return "mac" in os || "darwin" in os
}