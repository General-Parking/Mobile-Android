package io.mishkav.generalparking.ui.components.buttons

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.mishkav.generalparking.ui.theme.Typography

@Composable
fun IconTextButton(
    modifier: Modifier = Modifier,
    text: String,
    icon:  ImageVector,
    color: Color = MaterialTheme.colorScheme.onPrimary,
    onClick: () -> Unit
) = Button(
    onClick = onClick,
    elevation = ButtonDefaults.elevation(0.dp, 0.dp),
    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.surface),
    modifier = modifier
) {
    Icon(
        imageVector =  icon,
        contentDescription = "Localized description",
        tint = color
    )
    Text(
        text = text,
        color = color,
        style = Typography.subtitle1
    )
}