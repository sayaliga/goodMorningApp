package com.goodmorning.app.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColors = lightColorScheme()
private val DarkColors = darkColorScheme()

@Composable
fun GoodMorningTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val colors = if (darkTheme) DarkColors else LightColors

    SideEffect {
        systemUiController.setSystemBarsColor(colors.background)
    }

    MaterialTheme(
        colorScheme = colors,
        typography = GoodMorningTypography,
        content = content
    )
}
