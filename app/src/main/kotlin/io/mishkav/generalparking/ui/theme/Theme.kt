package io.mishkav.generalparking.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = Yellow400,
    onPrimary = Color.White,
    secondary = Yellow500,
    secondaryContainer = Yellow500.copy(alpha = 0.5f),
    surfaceVariant = Yellow500.copy(alpha = 0.1f),
    background = generalParkingDarkBackground,
    surface = generalParkingDarkBackground
)

private val LightColorPalette = lightColorScheme(
    primary = Yellow400,
    onPrimary = Color.Black,
    background = generalParkingLightBackground,
    surface = generalParkingLightBackground
)

@Composable
fun GeneralParkingTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme)
        DarkColorPalette
    else
        LightColorPalette

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        content = content
    )
}