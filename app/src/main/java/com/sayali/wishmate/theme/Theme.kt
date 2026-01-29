package com.sayali.wishmate.theme

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

// Dark theme (simple brand-friendly version)
private val DarkColorScheme = darkColorScheme(
    primary = NightBlue,
    onPrimary = Color.White,
    secondary = MorningYellow,
    onSecondary = TextBrown,
    tertiary = DiwaliOrange,
    background = Color(0xFF121212),
    onBackground = Color(0xFFEFEFEF),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFEFEFEF),
)

// Light theme using your cheerful palette
private val LightColorScheme = lightColorScheme(
    primary = MorningYellow,
    onPrimary = TextBrown,
    secondary = NightBlue,
    onSecondary = Color.White,
    tertiary = DiwaliOrange,

    background = BgGradientStart,
    onBackground = TextBrown,
    surface = Color.White,
    onSurface = TextBrown,
)

@Composable
fun GoodmorningAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // If you want STRICT WishMate colors, set this to false below.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme =
        if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        } else {
            if (darkTheme) DarkColorScheme else LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
