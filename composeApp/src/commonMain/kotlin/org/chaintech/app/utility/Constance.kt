package org.chaintech.app.utility

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val BottomNavigationBarHeight : Dp
    @Composable
    get() = 65.dp

data class SafeAreaSize(
    var top: Float = 0f,
    var bottom: Float = 0f,
)


fun getSafeAreaSize(): SafeAreaSize {
    return getSafeAreaHeight()
}
