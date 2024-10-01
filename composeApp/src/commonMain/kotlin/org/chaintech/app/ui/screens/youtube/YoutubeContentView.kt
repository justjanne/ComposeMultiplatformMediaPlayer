package org.chaintech.app.ui.screens.youtube

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.dp
import org.chaintech.app.font.FontType
import org.chaintech.app.font.MediaFont
import org.chaintech.app.model.MockData
import org.chaintech.app.model.VideoModel
import org.chaintech.app.navigation.LocalNavigation
import org.chaintech.app.theme.MyApplicationTheme
import org.chaintech.app.utility.FromRemote
import org.jetbrains.compose.resources.painterResource
import reelsdemo.composeapp.generated.resources.Res
import reelsdemo.composeapp.generated.resources.youtubeLogo

@Composable
fun YoutubeContentView() {
    val videoList = MockData().youtbeMockData.shuffled()
    val navigator = LocalNavigation.current

    Scaffold(
        topBar = { TopBarYouTube() }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(colors = MyApplicationTheme.colors.gradientPrimary)
                )
        ) {
            items(videoList) { video ->
                VideoItem(video) {
                    navigator.goToVideoPlayerScreen(video, videoList)
                }
            }
        }
    }
}

@Composable
private fun VideoItem(video: VideoModel, onClick: () -> Unit) {
    Column(
        modifier = Modifier.padding(vertical = 5.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(MyApplicationTheme.colors.border)
                .pointerInput(Unit) {
                    detectTapGestures { onClick() }
                }
        ) {
            FromRemote(
                painterResource = video.thumbL,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        VideoDetails(video)
    }
}

@Composable
private fun VideoDetails(video: VideoModel) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 12.dp)
    ) {
        Column {
            Text(
                text = video.title,
                style = MediaFont.lexendDeca(size = FontType.Regular, type = MediaFont.LexendDeca.Medium),
                color = MyApplicationTheme.colors.white,
                modifier = Modifier.padding(horizontal = 4.dp)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "${video.subtitle} - ${video.rating} views - ${video.date}",
                style = MediaFont.lexendDeca(size = FontType.Small, type = MediaFont.LexendDeca.Regular),
                color = MyApplicationTheme.colors.secondaryText,
                modifier = Modifier.padding(horizontal = 4.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = null,
            tint = Color.White
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarYouTube() {
    TopAppBar(
        title = {
            Image(
                painter = painterResource(Res.drawable.youtubeLogo),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(0.4f)
            )
        },
        actions = {
            IconButton(onClick = { /* TODO: Handle notifications */ }) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            IconButton(onClick = { /* TODO: Handle search */ }) {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        modifier = Modifier.background(Color.Black)
    )
}
