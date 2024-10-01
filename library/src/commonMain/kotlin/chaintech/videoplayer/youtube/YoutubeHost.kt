package chaintech.videoplayer.youtube

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume
import kotlin.time.Duration

sealed class YoutubePlayerState {
    object Idle : YoutubePlayerState()
    object Initialized : YoutubePlayerState()

    data class PlayingState(
        val videoId: String,
        val totalDuration: Duration = Duration.ZERO,
        val currentTime: Duration = Duration.ZERO,
        val isPlaying: Boolean = false,
        val playbackStatus: YoutubeEvent.StatusUpdated.PlaybackStatus = YoutubeEvent.StatusUpdated.PlaybackStatus.NOT_STARTED
    ) : YoutubePlayerState()

    data class ErrorState(val errorMessage: String) : YoutubePlayerState()
}

// Class to manage YouTube host state
@Stable
internal class YoutubeHost {
    private val lock = Mutex()

    var state by mutableStateOf<YoutubePlayerState>(YoutubePlayerState.Idle)
        private set

    var isBuffer by mutableStateOf(true)
        private set

    internal var currentCommand by mutableStateOf<YoutubeCommand?>(null)
    private var continuation: CancellableContinuation<Unit>? = null

    suspend fun loadVideo(id: String) = executeCommand(YoutubeCommand.Load(id))
    suspend fun play() = executeCommand(YoutubeCommand.Play)
    suspend fun pause() = executeCommand(YoutubeCommand.Pause)
    suspend fun seekTo(position: Duration) = executeCommand(YoutubeCommand.SeekTo(position))
    suspend fun seekBy(offset: Duration) = executeCommand(YoutubeCommand.SeekBy(offset))
    suspend fun mute() = executeCommand(YoutubeCommand.Mute)
    suspend fun unmute() = executeCommand(YoutubeCommand.Unmute)
    suspend fun setPlaybackRate(rate: Float) = executeCommand(YoutubeCommand.SetRate(rate))

    private suspend fun executeCommand(command: YoutubeCommand) {
        lock.withLock {
            try {
                suspendCancellableCoroutine {
                    this.continuation = it
                    this.currentCommand = command
                }
            } finally {
                this.currentCommand = null
                this.continuation = null
            }
        }
    }

    internal fun completeCommand() {
        continuation?.resume(Unit)
    }

    internal fun updateState(event: YoutubeEvent) {
        val newState:  YoutubePlayerState
        when (event) {
            is YoutubeEvent.ErrorOccurred -> {
                isBuffer = false
                newState = YoutubePlayerState.ErrorState(event.message)
            }
            is YoutubeEvent.VideoIdProcessed -> {
                isBuffer = false
                newState = YoutubePlayerState.PlayingState(event.id)
            }

            YoutubeEvent.Initialized -> {
                newState = YoutubePlayerState.Initialized
            }
            is YoutubeEvent.StatusUpdated -> {
                isBuffer = event.status == YoutubeEvent.StatusUpdated.PlaybackStatus.BUFFERING
                newState = when (val currentState = state) {
                    is YoutubePlayerState.PlayingState -> {
                        currentState.copy(
                            isPlaying = event.status == YoutubeEvent.StatusUpdated.PlaybackStatus.PLAYING,
                            playbackStatus = event.status
                        )
                    } else -> {
                        YoutubePlayerState.ErrorState("Unexpected state: $currentState")
                    }
                }
            }

            is YoutubeEvent.TimeUpdated -> {
                isBuffer = false
                newState = when (val currentState = state) {
                    is YoutubePlayerState.PlayingState -> {
                        currentState.copy(currentTime = event.currentTime)
                    } else -> {
                        YoutubePlayerState.ErrorState("Unexpected state: $currentState")
                    }
                }
            }

            is YoutubeEvent.DurationChanged -> {
                isBuffer = false
                newState = when (val currentState = state) {
                    is YoutubePlayerState.PlayingState -> {
                        currentState.copy(totalDuration = event.duration)
                    } else -> {
                        YoutubePlayerState.ErrorState("Unexpected state: $currentState")
                    }
                }
            }
        }
        state = newState
    }
}