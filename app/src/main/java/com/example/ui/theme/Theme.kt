package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF60A5FA),
    onPrimary = Color(0xFF0F1E36),
    primaryContainer = CathedralDeepBlue,
    onPrimaryContainer = Color(0xFFDBEAFE),
    secondary = CathedralGold,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF5A3D06),
    onSecondaryContainer = LightCathedralGold,
    tertiary = SafaricomGreen,
    onTertiary = Color.White,
    background = DarkSanctuaryBg,
    onBackground = DarkInkPrimary,
    surface = DarkSanctuarySurface,
    onSurface = DarkInkPrimary,
    surfaceVariant = Color(0xFF1B273A),
    onSurfaceVariant = DarkInkSecondary,
    outline = DarkSanctuaryBorder
)

private val LightColorScheme = lightColorScheme(
    primary = SanctuaryNavy,
    onPrimary = Color.White,
    primaryContainer = SoftSanctuaryBlue,
    onPrimaryContainer = SanctuaryNavy,
    secondary = CathedralGold,
    onSecondary = Color.White,
    secondaryContainer = LightCathedralGold,
    onSecondaryContainer = OchreGold,
    tertiary = SafaricomGreen,
    onTertiary = Color.White,
    background = ParchmentLight,
    onBackground = InkPrimary,
    surface = ParchmentCard,
    onSurface = InkPrimary,
    surfaceVariant = Color(0xFFF2ECE1),
    onSurfaceVariant = InkSecondary,
    outline = ParchmentBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
