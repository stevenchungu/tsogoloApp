package com.example.tsogolo.ui.theme

import android.content.Context
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import com.example.tsogolo.util.AppPreferences

private val DarkColorPalette = darkColors(
    primary = Emarald500,
    primaryVariant = Emarald700,
    secondary = Emarald200,
    background = Slate800,
    onBackground = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onSurface = Color.White
)

private val LightColorPalette = lightColors(
    primary = Emarald700,
    primaryVariant = Emarald700,
    secondary = Emarald500,
    background = Color.White,
    onSurface = Color.Black

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun TsogoloTheme(context: Context, content: @Composable () -> Unit) {
    val darkTheme = AppPreferences.getInstance(context = context).isDarkTheme.collectAsState()
    val colors = if (darkTheme.value) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}