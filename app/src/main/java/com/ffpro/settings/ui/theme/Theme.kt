package com.ffpro.settings.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppColorScheme = darkColorScheme(
    primary = NeonGreen,
    onPrimary = DeepBlack,
    secondary = NeonOrange,
    onSecondary = DeepBlack,
    background = DeepBlack,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = Color(0x1AFFFFFF),
    onSurfaceVariant = TextSecondary,
    error = AlertRed
)

@Composable
fun ProPlayerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
