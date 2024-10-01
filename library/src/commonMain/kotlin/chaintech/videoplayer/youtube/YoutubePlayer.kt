package chaintech.videoplayer.youtube

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLData

private const val BASE_URL = "https://www.youtube.com"
private const val BASE_MIME_TYPE = "text/html"
private const val BASE_ENCODING = "utf-8"

@Composable
internal fun YoutubePlayer(
    modifier: Modifier = Modifier,
    host: YoutubeHost,
    onEvent: ((YoutubeEvent) -> Unit)? = null,
) {
    val webViewState = rememberWebViewStateWithHTMLData(
        data = htmlContent,
        baseUrl = BASE_URL,
        mimeType = BASE_MIME_TYPE,
        encoding = BASE_ENCODING,
    )

    val navigator = rememberWebViewNavigator()
    val command = host.currentCommand

    webViewState.webSettings.apply {
        isJavaScriptEnabled = true
        androidWebSettings.apply {
            isAlgorithmicDarkeningAllowed = true
            safeBrowsingEnabled = false
            domStorageEnabled = true
            supportZoom = false
        }
    }

    LaunchedEffect(command) {
        command?.let {
            executeCommand(navigator, it)
            host.completeCommand()
        }
    }

    YoutubeActionHandler.processAction(webViewState.pageTitle)?.let { event ->
        host.updateState(event)
        onEvent?.invoke(event)
    }

    WebView(
        modifier = modifier.fillMaxSize(),
        state = webViewState,
        navigator = navigator,
    )
}

internal expect fun executeCommand(
    navigator: WebViewNavigator,
    execCommand: YoutubeCommand,
)
