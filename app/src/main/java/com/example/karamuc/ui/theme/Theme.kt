package com.example.karamuc.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primaryContainer = Color(0xFF444444),
    onPrimaryContainer = Color(0xFFa1a1a1),
    primary = Color(0xFF2196f3),
    onPrimary = Color.White,
    secondary = Color(0xFFff9800),
    onSecondary = Color.White,
    tertiary = Color(0xFF333333),
    onTertiary = Color(0xFFe5e6eb),
    background = Color(0xFF333333),
    onBackground = Color(0xFFe5e6eb)
)

private val LightColorScheme = lightColorScheme(
    primaryContainer = Color(0xFF444444),
    onPrimaryContainer = Color(0xFF222222),
    primary = Color(0xFF2196f3),
    onPrimary = Color.White,
    secondary = Color(0xFFff9800),
    onSecondary = Color.White,
    tertiary = Color(0xFF333333),
    onTertiary = Color(0xFFe5e6eb),
    background = Color(0xFF333333),
    onBackground = Color(0xFFe5e6eb),
)

@Composable
fun KaramucTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.onPrimaryContainer.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}