package io.mishkav.generalparking.ui.components.buttons

import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.theme.Shapes
import io.mishkav.generalparking.ui.theme.generalParkingLightBackground

@Composable
fun SimpleIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) = Button(
    onClick = onClick,
    shape = Shapes.medium,
    colors = ButtonDefaults.buttonColors(backgroundColor = color),
    modifier = modifier
) {
    Icon(
        imageVector = icon,
        contentDescription = stringResource(R.string.space),
        tint = generalParkingLightBackground
    )
}