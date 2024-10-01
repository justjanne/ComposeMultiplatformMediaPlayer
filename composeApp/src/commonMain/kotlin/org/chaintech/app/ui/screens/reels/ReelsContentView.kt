package org.chaintech.app.ui.screens.reels

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import chaintech.videoplayer.model.PlayerConfig
import chaintech.videoplayer.ui.reel.ReelsPlayerView
import org.chaintech.app.model.MockData
import androidx.compose.ui.unit.dp

@Composable
fun ReelsContentView() {
    ReelsPlayerView(
        modifier = Modifier
            .fillMaxSize(),
        urls = MockData().reelsUrlArray,
        playerConfig = PlayerConfig(
            isPauseResumeEnabled  = true,
            isSeekBarVisible = false,
            pauseResumeIconSize = 32.dp,
            reelVerticalScrolling = true,
            isFastForwardBackwardEnabled = false,
            isMuteControlEnabled = false,
            isSpeedControlEnabled = false,
            isFullScreenEnabled = false,
            isScreenLockEnabled = false,
            isScreenResizeEnabled = false
        )
    )
}
