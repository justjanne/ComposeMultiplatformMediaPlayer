package chaintech.videoplayer.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import chaintech.videoplayer.model.PlayerSpeed
import kotlinx.coroutines.delay


@Composable
internal fun DesktopSpeedSelection(
    buttonSize: Dp,
    selectedSpeed: PlayerSpeed,
    onSelectSpeed: ((PlayerSpeed?) -> Unit)
) {
    LaunchedEffect(selectedSpeed) {
        delay(5000) // Wait for 5 seconds
        onSelectSpeed(null)
    }
    Column {
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {

            SpeedButtonRow(
                buttonSize = buttonSize,
                selectedSpeed = selectedSpeed,
                onSelectSpeed = onSelectSpeed
            )
        }
    }
}

@Composable
private fun SpeedButtonRow(
    buttonSize: Dp,
    selectedSpeed: PlayerSpeed,
    onSelectSpeed: (PlayerSpeed?) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 35.dp),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f)) // Spacer to push buttons to the right

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