package org.chaintech.app.ui.screens.videoplayerview

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import chaintech.videoplayer.model.PlayerConfig
import chaintech.videoplayer.ui.video.VideoPlayerView
import chaintech.videoplayer.util.isDesktop
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.chaintech.app.font.FontType
import org.chaintech.app.font.MediaFont
import org.chaintech.app.model.MockData
import org.chaintech.app.model.VideoModel
import org.chaintech.app.navigation.LocalNavigation
import org.chaintech.app.theme.MyApplicationTheme
import org.chaintech.app.ui.components.AddBanner
import org.chaintech.app.ui.components.BackButtonNavBar
import org.chaintech.app.utility.FromLocalDrawable
import org.chaintech.app.utility.FromRemote
import org.chaintech.app.utility.getSafeAreaSize
import org.jetbrains.compose.resources.DrawableResource
import reelsdemo.composeapp.generated.resources.Res
import reelsdemo.composeapp.generated.resources.icn_add
import reelsdemo.composeapp.generated.resources.icn_download
import reelsdemo.composeapp.generated.resources.icn_share

class VideoPlayerView(private val currentVideo: VideoModel, private val videoList: List<VideoModel>) : Screen {
    @Composable
    override fun Content() {
        VideoPlayerContentView(currentVideo, videoList)
    }
}

@Composable
private fun VideoPlayerContentView(currentVideo: VideoModel, videoList: List<VideoModel>) {
    val navigator = LocalNavigation.current
    var video by remember { mutableStateOf(currentVideo) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = MyApplicationTheme.colors.gradientPrimary))
            .padding(top = getSafeAreaSize().top.dp),
        horizontalAlignment = Alignment.Start
    ) {
        if (isDesktop()) {
            BackButtonNavBar { navigator.back() }
        }

        VideoPlayerBox(video)

        if (!isDesktop()) {
            Spacer(modifier = Modifier.height(4.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                item(span = { GridItemSpan(2) }) {
                    videoDetails(video)
                }
                items(MockData().getFilteredData(videoList, video)) { item ->
                    VideoThumbnail(item) { video = item }
                }
                item(span = { GridItemSpan(2) }) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun VideoPlayerBox(video: VideoModel) {
    val navigator = LocalNavigation.current
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopStart
    ) {
        VideoPlayerView(
            modifier = Modifier.then(
                if (isDesktop()) Modifier.fillMaxSize() else Modifier.height(224.dp).fillMaxWidth()
            ),
            url = video.sources,
            playerConfig = PlayerConfig(
                seekBarActiveTrackColor = Color.Red,
                seekBarInactiveTrackColor = Color.White,
                seekBarBottomPadding = 8.dp,
                pauseResumeIconSize = if (isDesktop()) 18.dp else 30.dp,
                controlHideIntervalSeconds = 5,
                topControlSize = if (isDesktop()) 16.dp else 20.dp,
                durationTextStyle = MediaFont.lexendDeca(
                    size = if (isDesktop()) FontType.ExtraSmall else FontType.Regular,
                    type = MediaFont.LexendDeca.Medium
                ),
                fastForwardBackwardIconSize = if (isDesktop()) 16.dp else 28.dp,
                controlTopPadding = 10.dp
            )
        )

        if (!isDesktop()) {
            BackButtonNavBar { navigator.back() }
        }
    }
}

@Composable
private fun videoDetails(video: VideoModel) {
    Row(
        modifier = Modifier.padding(vertical = 13.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = video.title,
                style = MediaFont.lexendDeca(size = FontType.SubHeading, type = MediaFont.LexendDeca.Medium),
                color = MyApplicationTheme.colors.white,
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = video.subtitle,
                style = MediaFont.lexendDeca(size = FontType.Small, type = MediaFont.LexendDeca.Regular),
                color = MyApplicationTheme.colors.secondaryText,
                modifier = Modifier.padding(horizontal = 4.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            downLoadView()

            Spacer(modifier = Modifier.height(8.dp))

            AddBanner(title = "Demon Slayer Season 1 &2", image = MockData().detailBanner, padding = 5.dp)

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "More Like This",
                style = MediaFont.lexendDeca(size = FontType.SubHeading, type = MediaFont.LexendDeca.Medium),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MyApplicationTheme.colors.white,
                modifier = Modifier.padding(horizontal = 4.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun downLoadView() {
    Row(
        modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        downloadItem(Res.drawable.icn_add, "Watchlist")
        downloadItem(Res.drawable.icn_share, "Share", size=14.dp, modifier = Modifier.padding(top = 4.dp))
        downloadItem(Res.drawable.icn_download, "Download")
    }
}

@Composable
private fun downloadItem(image: DrawableResource, title: String, size: Dp=19.dp, modifier: Modifier = Modifier) {
    Column(horizontalAlignment=Alignment.CenterHorizontally, modifier = modifier) {
        FromLocalDrawable(painterResource=image, contentScale=ContentScale.Fit, modifier=Modifier.size(size))

        Spacer(modifier=Modifier.weight(1f))

        Text(
            text=title,
            style=MediaFont.lexendDeca(size=FontType.Small, type=MediaFont.LexendDeca.Regular),
            color=MyApplicationTheme.colors.white,
            maxLines=1
        )
    }
}

@Composable
private fun VideoThumbnail(video: VideoModel, onClick: () -> Unit) {
    Column(modifier=Modifier.padding(4.dp)) {
        Box(
            modifier=Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MyApplicationTheme.colors.border)
                .border(width=1.dp, color=MyApplicationTheme.colors.border, shape=RoundedCornerShape(8.dp))
                .pointerInput(Unit) { detectTapGestures { onClick() } }
        ) {
            FromRemote(painterResource=video.thumb, contentScale=ContentScale.Crop, modifier=Modifier.fillMaxSize())
        }
    }
}
