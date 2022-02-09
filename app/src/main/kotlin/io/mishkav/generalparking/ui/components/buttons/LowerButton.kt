package io.mishkav.generalparking.ui.components.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.mishkav.generalparking.ui.theme.Shapes
import io.mishkav.generalparking.ui.theme.Typography
import io.mishkav.generalparking.ui.theme.Yellow

@Composable
fun LowerButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) = Box(
    modifier = modifier.fillMaxWidth()
) {
    Button(
        onClick = onClick,
        shape = Shapes.large,
        colors = ButtonDefaults.buttonColors(backgroundColor = Yellow),
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
    ) {
        Text(
            text = text,
            color = Color.White,
            style = Typography.button
        )
    }
}