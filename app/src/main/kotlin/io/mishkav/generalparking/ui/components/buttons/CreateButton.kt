package io.mishkav.generalparking.ui.components.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.mishkav.generalparking.ui.theme.Typography
import io.mishkav.generalparking.ui.theme.Yellow

@Composable
fun CreateButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) = Button(
    onClick = onClick,
    elevation = ButtonDefaults.elevation(0.dp, 0.dp),
    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
    modifier = modifier
) {
    Text(
        text = text,
        color = Yellow,
        style = Typography.subtitle1
    )
}