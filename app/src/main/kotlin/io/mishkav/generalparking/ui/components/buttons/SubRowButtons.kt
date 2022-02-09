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
fun SubRowButtons(
    modifier: Modifier = Modifier,
    text1: String,
    text2: String,
    onClick1: () -> Unit,
    onClick2: () -> Unit
) = Row(
    horizontalArrangement = Arrangement.SpaceEvenly,
    modifier = modifier
        .fillMaxWidth()

) {
    Button(
        onClick = onClick1,
        elevation = ButtonDefaults.elevation(0.dp, 0.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
    ) {
        Text(
            text = text1,
            color = Color.Black,
            style = Typography.subtitle1
        )
    }
    Button(
        onClick = onClick2,
        elevation = ButtonDefaults.elevation(0.dp, 0.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
    ) {
        Text(
            text = text2,
            color = Yellow,
            style = Typography.subtitle1
        )
    }
}