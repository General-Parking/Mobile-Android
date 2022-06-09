package io.mishkav.generalparking.ui.components.buttons

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.theme.Typography

@Composable
fun SimpleIconTextButton(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
    color: Color = MaterialTheme.colorScheme.onPrimary,
    onClick: () -> Unit
) = Button(
    onClick = onClick,
    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.surface),
    modifier = modifier
) {
    Icon(
        imageVector = icon,
        contentDescription = stringResource(R.string.space),
        tint = color
    )
    Text(
        text = text,
        color = color,
        style = Typography.subtitle1,
        modifier = Modifier.padding(start = 5.dp)
    )
}