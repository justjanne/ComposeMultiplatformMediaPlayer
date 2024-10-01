package org.chaintech.app.font

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import reelsdemo.composeapp.generated.resources.Res
import reelsdemo.composeapp.generated.resources.lexenddeca_bold
import reelsdemo.composeapp.generated.resources.lexenddeca_medium
import reelsdemo.composeapp.generated.resources.lexenddeca_regular

object MediaFont {
    sealed class LexendDeca(val value: String, val fontWeight: FontWeight, val fontStyle: FontStyle) {
        data object Regular : LexendDeca("lexenddeca_regular", FontWeight.Normal, FontStyle.Normal)
        data object Medium : LexendDeca("lexenddeca_medium", FontWeight.Normal, FontStyle.Normal)
        data object Bold : LexendDeca("lexenddeca_bold", FontWeight.Normal, FontStyle.Normal)
    }

    @Composable
    fun lexendDeca(size: FontType, type: LexendDeca): TextStyle {
        val fontType = when(type) {
            LexendDeca.Regular -> Font(Res.font.lexenddeca_regular, FontWeight.Normal, FontStyle.Normal)
            LexendDeca.Medium -> Font(Res.font.lexenddeca_medium, FontWeight.Normal, FontStyle.Normal)
            LexendDeca.Bold -> Font(Res.font.lexenddeca_bold, FontWeight.Normal, FontStyle.Normal)
        }
        val fontFamily = fontType.toFontFamily()
        return TextStyle(
            fontSize = size.fontSize.sp,
            fontFamily = fontFamily
        )
    }
}

enum class FontType {
    ExtraLarge35,
    ExtraLarge30,
    ExtraLarge28,
    ExtraLarge26,
    ExtraLarge24,
    ExtraLarge,
    Heading,
    SubHeading,
    Regular,
    Small,
    ExtraSmall;

    val fontSize: Int
        get() = when (this) {
            ExtraLarge35 -> 32
            ExtraLarge30 -> 28
            ExtraLarge28 -> 26
            ExtraLarge26 -> 23
            ExtraLarge24 -> 22
            ExtraLarge -> 18
            Heading -> 16
            SubHeading -> 14
            Regular -> 12
            Small -> 10
            ExtraSmall -> 8
        }
}
