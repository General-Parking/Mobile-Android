package io.mishkav.generalparking.ui.components.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import io.mishkav.generalparking.ui.theme.Shapes
import io.mishkav.generalparking.ui.theme.Typography
import io.mishkav.generalparking.ui.theme.generalParkingLightBackground

@Composable
fun TextButton(
    modifier: Modifier = Modifier,
    shape: Shape = Shapes.large,
    text: String,
    onClick: () -> Unit
) = Box(
    modifier = modifier.fillMaxWidth()
) {
    Button(
        onClick = onClick,
        shape = shape,
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
    ) {
        Text(
            text = text,
            color = generalParkingLightBackground,
            style = Typography.button
        )
    }
}