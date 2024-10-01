package chaintech.videoplayer.youtube

import com.multiplatform.webview.web.WebViewNavigator

private const val COMMAND_EXECUTOR_PATTERN = "javascript:%s"

internal actual fun executeCommand(
    navigator: WebViewNavigator,
    execCommand: YoutubeCommand
) {
    navigator.loadUrl(COMMAND_EXECUTOR_PATTERN.format(execCommand.toCommandString()))
}