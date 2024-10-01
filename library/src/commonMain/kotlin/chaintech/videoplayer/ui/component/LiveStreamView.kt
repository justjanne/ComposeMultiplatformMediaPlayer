package chaintech.videoplayer.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import chaintech.videoplayer.model.PlayerConfig

@Composable
internal fun LiveStreamView(
    playerConfig: PlayerConfig
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart) // Align the column to the bottom
                .padding(bottom = playerConfig.seekBarBottomPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End // Align items to the end of the row
            ) {
                // Live indicator container
                Row(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.2f), shape = RoundedCornerShape(3.dp))
                        .padding(vertical = 4.dp, horizontal = 8.dp), // Add padding for better appearance
                    horizontalArrangement = Arrangement.spacedBy(5.dp), // Space between elements
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Live dot indicator
                    Box(
                        modifier = Modifier
                            .size(10.dp) // Size of the dot
                            .background(Color.Red, shape = CircleShape) // Red color and circular shape
                    )

                    // Display the "Live" text
                    Text(
                        text = "Live", // Indicate that this is a live stream
                        color = playerConfig.durationTextColor,
                        style = playerConfig.durationTextStyle,
                        modifier = Modifier.padding(end = 6.dp) // Padding at the end for spacing
                    )
                }
            }
        }
    }
}
