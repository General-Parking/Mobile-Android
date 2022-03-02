package io.mishkav.generalparking.ui.components.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.mishkav.generalparking.ui.theme.BottomColor
import io.mishkav.generalparking.ui.theme.Shapes
import io.mishkav.generalparking.ui.theme.Typography
import io.mishkav.generalparking.ui.theme.generalParkingLightBackground

@Composable
fun BottomTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) = Box(
    modifier = modifier.fillMaxWidth()
) {
    Button(
        onClick = onClick,
        shape = Shapes.small,
        colors = ButtonDefaults.buttonColors(backgroundColor = BottomColor),
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