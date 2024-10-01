package chaintech.videoplayer.ui.video.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import chaintech.videoplayer.model.PlayerConfig
import chaintech.videoplayer.model.PlayerSpeed
import chaintech.videoplayer.model.gradientBGColors
import chaintech.videoplayer.ui.component.SpeedSelectionView

@Composable
fun SpeedSelectionOverlay(
    playerConfig: PlayerConfig,
    selectedSpeed: PlayerSpeed,
    selectedSpeedCallback: (PlayerSpeed) -> Unit,
    showSpeedSelection: Boolean,
    showSpeedSelectionCallback: (Boolean) -> Unit
) {
    Box {
        // Playback speed options popup background
        AnimatedVisibility(
            visible = showSpeedSelection,
            enter = fadeIn(),
            exit = fadeOut(animationSpec = tween(durationMillis = 700))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(brush = Brush.horizontalGradient(gradientBGColors))
                    .pointerInput(Unit) {
                        detectTapGestures {
                            showSpeedSelectionCallback(false) // Hide overlay on tap outside
                        }
                    }
            )
        }

        // Playback speed options popup
        AnimatedVisibility(
            visible = showSpeedSelection,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth }, // Start from the right edge
                animationSpec = tween(durationMillis = 500) // Animation duration
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth }, // Slide out to the right edge
                animationSpec = tween(durationMillis = 500) // Animation duration
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                SpeedSelectionView(
                    buttonSize = (playerConfig.topControlSize * 1.25f),
                    selectedSpeed = selectedSpeed,
                    onSelectSpeed = { speed ->
                        speed?.let { selectedSpeedCallback(it) }
                        showSpeedSelectionCallback(false) // Hide overlay after selection
                    }
                )
            }
        }
    }
}