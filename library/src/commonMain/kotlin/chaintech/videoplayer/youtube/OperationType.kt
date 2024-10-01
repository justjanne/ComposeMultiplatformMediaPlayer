package chaintech.videoplayer.youtube

internal enum class OperationType(val operationName: String) {
    INITIALIZED("onReady"),
    FAILURE("onError"),
    DURATION_UPDATED("onVideoDuration"),
    STATUS_CHANGED("onStateChange"),
    TIME_UPDATED("onCurrentTimeChange"),
    VIDEO_ID_PROCESSED("onVideoId");

    companion object {
        fun fromNameOrNull(name: String): OperationType? {
            return entries.firstOrNull { it.operationName == name }
        }
    }
}