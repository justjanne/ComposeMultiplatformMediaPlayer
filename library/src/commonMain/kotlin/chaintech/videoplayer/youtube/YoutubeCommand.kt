package chaintech.videoplayer.youtube

import kotlin.time.Duration

sealed interface YoutubeCommand {
    fun toCommandString(): String

    data class Load(val id: String, val start: Duration = Duration.ZERO) : YoutubeCommand {
        override fun toCommandString(): String = "loadVideo('$id', ${start.inWholeSeconds});"
    }

    object Play : YoutubeCommand {
        override fun toCommandString(): String = "playVideo();"
    }

    object Pause : YoutubeCommand {
        override fun toCommandString(): String = "pauseVideo();"
    }

    data class SeekTo(val position: Duration) : YoutubeCommand {
        override fun toCommandString(): String = "seekTo(${position.inWholeSeconds});"
    }

    data class SeekBy(val offset: Duration) : YoutubeCommand {
        override fun toCommandString(): String = "seekBy(${offset.inWholeSeconds});"
    }

    object Mute : YoutubeCommand {
        override fun toCommandString(): String = "mute();"
    }

    object Unmute : YoutubeCommand {
        override fun toCommandString(): String = "unMute();"
    }

    data class SetRate(val rate: Float) : YoutubeCommand {
        override fun toCommandString(): String = "setPlaybackRate($rate);"
    }
}