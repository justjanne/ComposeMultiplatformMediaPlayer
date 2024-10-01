package chaintech.videoplayer.ui.video.controls

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chaintech.videoplayer.model.PlayerConfig
import chaintech.videoplayer.ui.component.AnimatedClickableIcon

@Composable
internal fun LockScreenView(
    playerConfig: PlayerConfig,
    showControls: Boolean,
    onLockScreenToggle: (() -> Unit),
) {
    // Layout structure: Box containing a Column
    Box(
        modifier = Modifier.fillMaxSize() // Fill the available space
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopStart) // Align the column to the top start
                .padding(top = playerConfig.controlTopPadding) // Add padding to the top
        ) {
            // Show controls with animation based on the visibility flag
            AnimatedVisibility(
                visible = showControls, // Visibility flag
                enter = fadeIn(), // Fade in animation when controls are shown
                exit = fadeOut() // Fade out animation when controls are hidden
            ) {
                // Row to contain control icons
                Row(
                    modifier = Modifier.fillMaxWidth() // Fill the available width
                        .padding(horizontal = 16.dp), // Add horizontal padding
                    verticalAlignment = Alignment.Top, // Align items to the top vertically
                    horizontalArrangement =  Arrangement.End
                ) {
                    AnimatedClickableIcon(
                        imageVector = Icons.Default.Lock ,
                        contentDescription = "Lock", // Accessibility description
                        tint = playerConfig.iconsTintColor, // Icon color
                        iconSize = playerConfig.topControlSize, // Icon size
                        onClick = { onLockScreenToggle() } // Toggle Lock on click
                    )
                }
            }
        }
    }
}

