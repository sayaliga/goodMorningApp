package com.example.goodmorning.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = SkyPrimary,
    onPrimary = CalmOnPrimary,
    secondary = DawnSecondary,
    background = SoftBackground,
    surface = CalmOnPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = TextSecondary
)

private val DarkColors = darkColorScheme(
    primary = SkyPrimary,
    onPrimary = CalmOnPrimary,
    secondary = DawnSecondary,
    background = MidnightSurface,
    surface = MidnightSurface,
    onBackground = CalmOnPrimary,
    onSurface = CalmOnPrimary,
    surfaceVariant = TextSecondary
)

@Composable
fun GoodMorningTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    colorScheme: ColorScheme = if (useDarkTheme) DarkColors else LightColors,
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        window.statusBarColor = colorScheme.background.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !useDarkTheme
    }

    CompositionLocalProvider(LocalSpacing provides Spacing()) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = GoodMorningTypography,
            content = content
        )
    }
}
