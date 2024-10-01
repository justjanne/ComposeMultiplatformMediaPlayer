package chaintech.videoplayer.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import chaintech.videoplayer.model.PlayerSpeed
import chaintech.videoplayer.model.selectedSpeedButtonColor
import chaintech.videoplayer.model.selectedTextColor
import chaintech.videoplayer.model.unselectedSpeedButtonColor
import chaintech.videoplayer.model.unselectedTextColor
import kotlinx.coroutines.delay

@Composable
internal fun SpeedSelectionView(
    buttonSize: Dp,
    selectedSpeed: PlayerSpeed,
    onSelectSpeed: (PlayerSpeed?) -> Unit
) {
    // Reset speed selection after 5 seconds
    LaunchedEffect(selectedSpeed) {
        delay(5000) // Wait for 5 seconds
        onSelectSpeed(null)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            SpeedButtonColumn(
                buttonSize = buttonSize,
                selectedSpeed = selectedSpeed,
                onSelectSpeed = onSelectSpeed
            )
        }
    }
}

@Composable
private fun SpeedButtonColumn(
    buttonSize: Dp,
    selectedSpeed: PlayerSpeed,
    onSelectSpeed: (PlayerSpeed?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(horizontal = 35.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp, alignment = Alignment.CenterVertically)
    ) {
        PlayerSpeedButton(
            title = "0.5x",
            size = buttonSize,
            backgroundColor = getButtonColor(selectedSpeed, PlayerSpeed.X0_5),
            titleColor = getTextColor(selectedSpeed, PlayerSpeed.X0_5),
            onClick = { onSelectSpeed(PlayerSpeed.X0_5) }
        )

        PlayerSpeedButton(
            title = "1.0x",
            size = buttonSize,
            backgroundColor = getButtonColor(selectedSpeed, PlayerSpeed.X1),
            titleColor = getTextColor(selectedSpeed, PlayerSpeed.X1),
            onClick = { onSelectSpeed(PlayerSpeed.X1) }
        )

        PlayerSpeedButton(
            title = "1.5x",
            size = buttonSize,
            backgroundColor = getButtonColor(selectedSpeed, PlayerSpeed.X1_5),
            titleColor = getTextColor(selectedSpeed, PlayerSpeed.X1_5),
            onClick = { onSelectSpeed(PlayerSpeed.X1_5) }
        )

        PlayerSpeedButton(
            title = "2.0x",
            size = buttonSize,
            backgroundColor = getButtonColor(selectedSpeed, PlayerSpeed.X2),
            titleColor = getTextColor(selectedSpeed, PlayerSpeed.X2),
            onClick = { onSelectSpeed(PlayerSpeed.X2) }
        )
    }
}

// Helper function to determine button color based on selection
@Composable
internal fun getButtonColor(selectedSpeed: PlayerSpeed, speed: PlayerSpeed): Color {
    return if (selectedSpeed == speed) {
        selectedSpeedButtonColor
    } else {
        unselectedSpeedButtonColor
    }
}

// Helper function to determine text color based on selection
@Composable
internal fun getTextColor(selectedSpeed: PlayerSpeed, speed: PlayerSpeed): Color {
    return if (selectedSpeed == speed) {
        selectedTextColor
    } else {
        unselectedTextColor
    }
}