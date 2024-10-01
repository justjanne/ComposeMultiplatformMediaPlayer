package chaintech.videoplayer.youtube

import com.multiplatform.webview.web.WebViewNavigator

internal actual fun executeCommand(
    navigator: WebViewNavigator,
    execCommand: YoutubeCommand
) {
    navigator.evaluateJavaScript(execCommand.toCommandString())
}
