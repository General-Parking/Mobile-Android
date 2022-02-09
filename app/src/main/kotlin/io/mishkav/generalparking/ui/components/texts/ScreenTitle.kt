package io.mishkav.generalparking.ui.components.texts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.mishkav.generalparking.ui.theme.Typography

@Composable
fun ScreenTitle(
    modifier: Modifier = Modifier,
    text: String
) = Box(
    modifier = modifier.fillMaxWidth()
) {
    Text(
        text = text,
        style = Typography.h4,
        modifier = Modifier
            .padding(vertical = 10.dp)
            .align(Alignment.BottomCenter)
    )
}