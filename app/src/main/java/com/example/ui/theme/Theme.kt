package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ToyverseDarkColorScheme = darkColorScheme(
    primary = ChampagneGold,
    onPrimary = MidnightNavy,
    primaryContainer = RoyalGoldAmber,
    onPrimaryContainer = PaleGold,

    secondary = RoyalCosmicPurple,
    onSecondary = Color.White,
    secondaryContainer = DeepCosmicViolet,
    onSecondaryContainer = LavenderTint,

    tertiary = ElectricCyan,
    onTertiary = MidnightNavy,
    tertiaryContainer = Color(0xFF093144),
    onTertiaryContainer = Color(0xFFA6F3FF),

    background = MidnightNavy,
    onBackground = WarmIvory,

    surface = DeepCosmicNavy,
    onSurface = WarmIvory,
    surfaceVariant = NebulaSurface,
    onSurfaceVariant = StarlightMuted,
    surfaceContainer = ElevatedNavy,

    outline = GlassBorder,
    outlineVariant = MutedDivider,

    error = CoralPink,
    onError = Color.White
)

private val ToyverseLightColorScheme = lightColorScheme(
    primary = Color(0xFF946200),
    onPrimary = Color.White,
    primaryContainer = PaleGold,
    onPrimaryContainer = Color(0xFF2C1C00),

    secondary = RoyalCosmicPurple,
    onSecondary = Color.White,
    secondaryContainer = LavenderTint,
    onSecondaryContainer = DeepCosmicViolet,

    tertiary = Color(0xFF00677F),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFB5EBFF),
    onTertiaryContainer = Color(0xFF001F28),

    background = Color(0xFFF7F8FC),
    onBackground = Color(0xFF131728),

    surface = Color.White,
    onSurface = Color(0xFF131728),
    surfaceVariant = Color(0xFFE9ECF8),
    onSurfaceVariant = Color(0xFF434960),

    outline = Color(0xFFC7CBDC),
    outlineVariant = Color(0xFFDFE2F0),

    error = CoralPink,
    onError = Color.White
)

@Composable
fun ToyverseEliteTheme(
    darkTheme: Boolean = true, // Default to spectacular Midnight Universe dark mode
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) ToyverseDarkColorScheme else ToyverseLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    ToyverseEliteTheme(darkTheme = darkTheme, content = content)
}
