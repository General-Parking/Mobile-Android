package io.mishkav.generalparking.ui.components.texts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.mishkav.generalparking.ui.theme.Typography

@Composable
fun BottomBody(
    modifier: Modifier = Modifier,
    text: String
) = Box(
    modifier = modifier
        .fillMaxWidth()
) {
    Text(
        text = text,
        style = Typography.body1,
        color = MaterialTheme.colorScheme.onPrimary
    )
}