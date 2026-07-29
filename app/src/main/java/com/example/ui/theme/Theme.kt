package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = VedvoraPrimary,
    onPrimary = VedvoraOnPrimary,
    primaryContainer = VedvoraPrimaryContainer,
    onPrimaryContainer = VedvoraOnPrimaryContainer,
    secondary = VedvoraSecondary,
    onSecondary = Color.White,
    secondaryContainer = VedvoraSecondaryContainer,
    onSecondaryContainer = VedvoraOnSecondaryContainer,
    tertiary = VedvoraGold,
    onTertiary = Color.White,
    tertiaryContainer = VedvoraGoldContainer,
    onTertiaryContainer = VedvoraOnGoldContainer,
    background = VedvoraBackground,
    onBackground = VedvoraOnSurface,
    surface = VedvoraSurface,
    onSurface = VedvoraOnSurface,
    surfaceVariant = VedvoraSurfaceContainerLow,
    onSurfaceVariant = VedvoraOnSurfaceVariant,
    outline = VedvoraOutline,
    outlineVariant = VedvoraOutlineVariant,
    error = VedvoraError,
    errorContainer = VedvoraErrorContainer
)

private val DarkColorScheme = darkColorScheme(
    primary = VedvoraGold,
    onPrimary = Color(0xFF0F172A),
    primaryContainer = Color(0xFF1E293B),
    onPrimaryContainer = Color(0xFFF8FAFC),
    secondary = Color(0xFF10B981),
    onSecondary = Color(0xFF022C22),
    secondaryContainer = Color(0xFF064E3B),
    onSecondaryContainer = Color(0xFFA7F3D0),
    tertiary = VedvoraGoldLight,
    onTertiary = Color(0xFF451A03),
    background = Color(0xFF0B0F19),
    onBackground = Color(0xFFF1F5F9),
    surface = Color(0xFF111827),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF1F2937),
    onSurfaceVariant = Color(0xFF9CA3AF),
    outline = Color(0xFF6B7280),
    outlineVariant = Color(0xFF374151),
    error = Color(0xFFEF4444),
    errorContainer = Color(0xFF7F1D1D)
)

@Composable
fun VedvoraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    VedvoraTheme(darkTheme = darkTheme, content = content)
}
