package org.chaintech.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import org.chaintech.app.utility.FromLocalDrawable
import androidx.compose.ui.unit.dp
import reelsdemo.composeapp.generated.resources.Res
import reelsdemo.composeapp.generated.resources.ic_back_button

@Composable
fun BackButtonNavBar(
    modifier: Modifier = Modifier,
    onBackButtonClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 0.dp,
                spotColor = Color.Transparent
            ),
        verticalArrangement = Arrangement.Top
    ) {
        BackButtonContainer(onBackButtonClick)
    }
}

@Composable
private fun BackButtonContainer(onBackButtonClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Transparent)))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BackButton(onBackButtonClick)
        }
    }
}

@Composable
private fun BackButton(onBackButtonClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(top = 8.dp)
            .size(30.dp)
            .background(Color.Black.copy(alpha = 0.3f), shape = CircleShape)
            .pointerInput(Unit) {
                detectTapGestures { onBackButtonClick() }
            }
    ) {
        FromLocalDrawable(
            painterResource = Res.drawable.ic_back_button,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 5.dp)
        )
    }
}
