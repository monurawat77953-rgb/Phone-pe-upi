package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = PhonePePurpleLight,
    onPrimary = Color.White,
    primaryContainer = PhonePePurpleDark,
    onPrimaryContainer = PhonePePurpleContainer,
    secondary = PhonePeBlue,
    onSecondary = Color.White,
    secondaryContainer = PhonePePurpleDark,
    onSecondaryContainer = Color.White,
    tertiary = PhonePeAmber,
    onTertiary = Color.Black,
    background = PhonePeDarkBackground,
    onBackground = PhonePeDarkTextPrimary,
    surface = PhonePeDarkSurface,
    onSurface = PhonePeDarkTextPrimary,
    surfaceVariant = PhonePeDarkSurfaceVariant,
    onSurfaceVariant = PhonePeDarkTextSecondary,
    outline = Color(0xFF4B4358),
  )

private val LightColorScheme =
  lightColorScheme(
    primary = PhonePePurple,
    onPrimary = Color.White,
    primaryContainer = PhonePePurpleContainer,
    onPrimaryContainer = PhonePePurpleDeep,
    secondary = PhonePeBlue,
    onSecondary = Color.White,
    secondaryContainer = PhonePeBlueLight,
    onSecondaryContainer = PhonePeBlue,
    tertiary = PhonePeAmber,
    onTertiary = Color.White,
    tertiaryContainer = PhonePeAmberLight,
    onTertiaryContainer = Color(0xFF6B4300),
    background = PhonePeBackground,
    onBackground = PhonePeTextPrimary,
    surface = PhonePeSurface,
    onSurface = PhonePeTextPrimary,
    surfaceVariant = PhonePeSurfaceVariant,
    onSurfaceVariant = PhonePeTextSecondary,
    outline = PhonePeDivider,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Keep consistent PhonePe branding across all Android versions
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
