package chaintech.videoplayer.youtube

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

sealed class YoutubeEvent {
    object Initialized : YoutubeEvent()

    data class ErrorOccurred(val message: String) : YoutubeEvent()

    data class DurationChanged(val duration: Duration) : YoutubeEvent() {
        companion object {
            fun fromString(value: String): DurationChanged? =
                value.toDoubleOrNull()?.toDuration(DurationUnit.SECONDS)?.let { DurationChanged(it) }
        }
    }

    data class StatusUpdated(val status: PlaybackStatus) : YoutubeEvent() {
        enum class PlaybackStatus(val description: String) {
            NOT_STARTED("UNSTARTED"),
            FINISHED("ENDED"),
            PLAYING("PLAYING"),
            PAUSED("PAUSED"),
            BUFFERING("BUFFERING"),
            QUEUED("CUED");
        }

        companion object {
            fun fromString(value: String): StatusUpdated? =
                PlaybackStatus.entries.firstOrNull { it.description == value }?.let { StatusUpdated(it) }
        }
    }

    data class TimeUpdated(val currentTime: Duration) : YoutubeEvent() {
        companion object {
            fun fromString(value: String): TimeUpdated? =
                value.toDoubleOrNull()?.toDuration(DurationUnit.SECONDS)?.let { TimeUpdated(it) }
        }
    }

    data class VideoIdProcessed(val id: String) : YoutubeEvent() {
        companion object {
            fun fromString(value: String?): VideoIdProcessed? = value?.let { VideoIdProcessed(it) }
        }
    }

    companion object {
        internal fun fromOperationAndData(
            operation: OperationType?,
            data: String
        ): YoutubeEvent? = when (operation) {
            OperationType.INITIALIZED -> Initialized
            OperationType.FAILURE -> ErrorOccurred(data)
            OperationType.DURATION_UPDATED -> DurationChanged.fromString(data)
            OperationType.STATUS_CHANGED -> StatusUpdated.fromString(data)
            OperationType.TIME_UPDATED -> TimeUpdated.fromString(data)
            OperationType.VIDEO_ID_PROCESSED -> VideoIdProcessed.fromString(data)
            null -> null
        }
    }
}
