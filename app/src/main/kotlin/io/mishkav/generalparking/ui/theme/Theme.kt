package io.mishkav.generalparking.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColorScheme(
    primary = Yellow400,
    onPrimary = Color.White,
    secondary = Yellow500,
    secondaryContainer = Yellow500.copy(alpha = 0.5f),
    surfaceVariant = Yellow500.copy(alpha = 0.1f),
    background = generalParkingDarkBackground,
    surface = generalParkingDarkBackground,
    onBackground = Gray500
)

private val LightColorPalette = lightColorScheme(
    primary = Yellow400,
    onPrimary = Color.Black,
    background = generalParkingLightBackground,
    surface = generalParkingLightBackground,
    onBackground = Gray500
)

@Composable
fun GeneralParkingTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme)
        DarkColorPalette
    else
        LightColorPalette

    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = Color.Transparent
    )
    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        content = content
    )
}