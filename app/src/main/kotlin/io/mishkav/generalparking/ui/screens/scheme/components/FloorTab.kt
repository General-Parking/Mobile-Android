package io.mishkav.generalparking.ui.screens.scheme.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.mishkav.generalparking.ui.theme.Gray400

@Composable
fun FloorTab(
    floor: String,
    isCurrent: Boolean,
    onClick: () -> Unit = {}
) = Surface(
    shape = RoundedCornerShape(50),
    shadowElevation = 1.dp,
    color = if (isCurrent)
        Color.White
    else
        Gray400,
    border = if (isCurrent) {
        BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    } else {
        BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    },
    contentColor = MaterialTheme.colorScheme.onPrimary,
) {
    Box(
        modifier = Modifier
            .width(50.dp)
            .padding(vertical = 4.dp)
            .wrapContentHeight()
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(floor)
    }
}

@Composable
@Preview
fun FloorTabPreview() {
    Column {
        FloorTab(
            floor = "-1",
            isCurrent = true
        )
        FloorTab(
            floor = "0",
            isCurrent = false
        )
    }
}