package chaintech.videoplayer.youtube

internal object YoutubeActionHandler {

    private val actionPattern = "ytplayer://([A-z]+)(\\?data=([A-z\\d._-]+))*".toRegex()

    fun processAction(url: String?): YoutubeEvent? {
        val matchResult = actionPattern.matchEntire(url.orEmpty())
        return matchResult?.let { result ->
            val operationType = result.groupValues[1].let(OperationType.Companion::fromNameOrNull)
            val payload = result.groupValues[3]
            YoutubeEvent.fromOperationAndData(operationType, payload)
        }
    }
}