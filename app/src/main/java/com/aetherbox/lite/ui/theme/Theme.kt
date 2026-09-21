package com.aetherbox.lite.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.aetherbox.lite.R

enum class LitePalette(
    val displayName: String,
    val primaryLight: Color,
    val secondaryLight: Color,
    val tertiaryLight: Color,
    val primaryDark: Color,
    val secondaryDark: Color,
    val tertiaryDark: Color
) {
    AETHER(
        "Aether",
        Color(0xFF0B6E4F), Color(0xFF08A4A7), Color(0xFFE8C547),
        Color(0xFF3DFFB5), Color(0xFF5CE1E6), Color(0xFFF0D35E)
    ),
    NEBULA(
        "Nebula",
        Color(0xFF006D77), Color(0xFF0A9396), Color(0xFF94D2BD),
        Color(0xFF2EC4B6), Color(0xFF48CAE4), Color(0xFF90E0EF)
    ),
    OCEAN(
        "Ocean",
        Color(0xFF0277BD), Color(0xFF00ACC1), Color(0xFF26A69A),
        Color(0xFF4FC3F7), Color(0xFF4DD0E1), Color(0xFF80CBC4)
    ),
    GRAPHITE(
        "Graphite",
        Color(0xFF455A64), Color(0xFF607D8B), Color(0xFF78909C),
        Color(0xFFB0BEC5), Color(0xFF90A4AE), Color(0xFFCFD8DC)
    ),
    FOREST(
        "Forest",
        Color(0xFF2E7D32), Color(0xFF558B2F), Color(0xFF8D6E63),
        Color(0xFF81C784), Color(0xFFA5D6A7), Color(0xFFBCAAA4)
    );

    companion object {
        fun fromName(name: String): LitePalette =
            entries.find { it.name == name } ?: AETHER
    }
}

val SpaceGrotesk = FontFamily(
    Font(R.font.space_grotesk_regular, FontWeight.Normal),
    Font(R.font.space_grotesk_medium, FontWeight.Medium),
    Font(R.font.space_grotesk_bold, FontWeight.Bold)
)

val JetBrainsMono = FontFamily(
    Font(R.font.jetbrains_mono_regular, FontWeight.Normal),
    Font(R.font.jetbrains_mono_bold, FontWeight.Bold)
)

private val LiteTypography = Typography(
    displaySmall = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 42.sp,
        letterSpacing = (-0.6).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.2).sp
    ),
    titleLarge = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp
    ),
    titleSmall = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.15.sp
    ),
    bodySmall = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.2.sp
    ),
    labelLarge = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp
    ),
    labelMedium = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 3.2.sp
    ),
    labelSmall = TextStyle(
        fontFamily = SpaceGrotesk,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.5.sp
    )
)

@Composable
fun AetherBoxLiteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    palette: LitePalette = LitePalette.AETHER,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            primary = palette.primaryDark,
            onPrimary = Color(0xFF003825),
            secondary = palette.secondaryDark,
            onSecondary = Color(0xFF00363A),
            tertiary = palette.tertiaryDark,
            onTertiary = Color(0xFF3A3000),
            background = Color(0xFF121212),
            onBackground = Color(0xFFE2E2E6),
            surface = Color(0xFF1A1A1A),
            onSurface = Color(0xFFE2E2E6),
            surfaceVariant = Color(0xFF2B2B2F),
            onSurfaceVariant = Color(0xFFC6C6CA),
            outline = Color(0xFF909094),
            outlineVariant = Color(0xFF454549),
            error = Color(0xFFF2B8B5),
            onError = Color(0xFF601410)
        )
    } else {
        lightColorScheme(
            primary = palette.primaryLight,
            onPrimary = Color.White,
            secondary = palette.secondaryLight,
            onSecondary = Color.White,
            tertiary = palette.tertiaryLight,
            onTertiary = Color(0xFF1B1B1F),
            background = Color(0xFFFBFFFE),
            onBackground = Color(0xFF1B1B1F),
            surface = Color(0xFFF2F7F5),
            onSurface = Color(0xFF1B1B1F),
            surfaceVariant = Color(0xFFE6EFEC),
            onSurfaceVariant = Color(0xFF46464A),
            outline = Color(0xFF6B7F78),
            outlineVariant = Color(0xFFC6C6CA),
            error = Color(0xFFB3261E),
            onError = Color.White
        )
    }

    MaterialTheme(
        colorScheme = colors,
        typography = LiteTypography,
        content = content
    )
}
