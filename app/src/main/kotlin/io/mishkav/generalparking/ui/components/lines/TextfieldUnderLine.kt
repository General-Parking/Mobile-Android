package io.mishkav.generalparking.ui.components.lines

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.mishkav.generalparking.ui.theme.Gray500

@Composable
fun TextfieldUnderLine() = Box(
    modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(color = Gray500)
)