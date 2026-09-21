package com.aetherbox.lite.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Ink = Color(0xFF0B1F24)
val Lagoon = Color(0xFF0B3D4A)
val Seafoam = Color(0xFF5EC8B7)
val Mist = Color(0xFFE8F0F2)
val Fog = Color(0xFFC5D9DE)
val Foam = Color(0xFFF4FAFB)
val Ember = Color(0xFFC45C26)

// System families only — bundled variable TTFs were crash-prone on some devices.
private val DisplayFont = FontFamily.Serif
private val BodyFont = FontFamily.SansSerif

private val LiteColors = lightColorScheme(
    primary = Lagoon,
    onPrimary = Foam,
    secondary = Seafoam,
    onSecondary = Ink,
    background = Mist,
    onBackground = Ink,
    surface = Foam,
    onSurface = Ink,
    surfaceVariant = Fog,
    onSurfaceVariant = Lagoon,
    outline = Lagoon.copy(alpha = 0.28f)
)

private val LiteTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = DisplayFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 48.sp,
        lineHeight = 52.sp,
        letterSpacing = (-0.5).sp,
        color = Ink
    ),
    headlineMedium = TextStyle(
        fontFamily = DisplayFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        color = Ink
    ),
    titleLarge = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        color = Ink
    ),
    bodyLarge = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        color = Ink.copy(alpha = 0.82f)
    ),
    bodyMedium = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = Ink.copy(alpha = 0.72f)
    ),
    labelLarge = TextStyle(
        fontFamily = BodyFont,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.2.sp
    )
)

@Composable
fun AetherBoxLiteTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LiteColors,
        typography = LiteTypography,
        content = content
    )
}
