package chaintech.videoplayer.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun AnimatedClickableIcon(
    painterRes: DrawableResource? = null,
    imageVector: ImageVector? = null,
    contentDescription: String?,
    tint: Color, // Color to tint the icon
    iconSize: Dp, // Size of the icon
    animationDuration: Int = 300, // Duration of the scale animation in milliseconds
    colorFilter : ColorFilter? = null,
    onClick: () -> Unit // Callback to handle icon click
) {
    // State to track if the icon is clicked
    var isClicked by remember { mutableStateOf(false) }

    // Animated scale value based on the click state
    val scale by animateFloatAsState(
        targetValue = if (isClicked) 0.8f else 1f, // Scale down when clicked, revert to original size otherwise
        animationSpec = tween(durationMillis = animationDuration)
    )

    // Effect to reset the click state after the animation duration
    LaunchedEffect(isClicked) {
        if (isClicked) {
            delay(animationDuration.toLong()) // Wait for the animation to complete
            isClicked = false // Reset the click state
            onClick() // Call the onClick callback
        }
    }

    Box(
        modifier = Modifier
            .size(iconSize)
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures { _ ->
                    isClicked = true
                }
            }
    ) {
        // If an ImageVector is provided, draw it as an Icon
        if (painterRes != null) {
            Image(
                painter = painterResource(painterRes),
                contentDescription = contentDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize(),
                colorFilter = colorFilter
            )
        } else {
            imageVector?.let {
                Icon(
                    imageVector = imageVector,
                    contentDescription = contentDescription,
                    tint = tint,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
