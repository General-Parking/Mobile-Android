package io.mishkav.generalparking.ui.screens.map.mapScreen

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun MapScreen(
    navController: NavHostController,
    onError: @Composable (Int) -> Unit
) {
    Text(
        text = "HELLO WORLD",
        color = MaterialTheme.colorScheme.onPrimary
    )
}