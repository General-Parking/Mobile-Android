package io.mishkav.generalparking.ui.components.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.theme.Shapes
import io.mishkav.generalparking.ui.theme.Typography
import io.mishkav.generalparking.ui.theme.generalParkingLightBackground

@Composable
fun BottomTextButton(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.go_reserve),
    onClick: () -> Unit
) = Box(
    modifier = modifier.fillMaxWidth()
) {
    Button(
        onClick = onClick,
        shape = Shapes.small,
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
        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = stringResource(R.string.space),
            tint = generalParkingLightBackground
        )
    }
}