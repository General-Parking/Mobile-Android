package io.mishkav.generalparking.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.buttons.IconTextButton
import io.mishkav.generalparking.ui.theme.Gray500
import io.mishkav.generalparking.ui.theme.Shapes

@Composable
fun UnselectedSchemeContent(
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Box(
            Modifier
                .clip(Shapes.medium)
                .size(20.dp)
                .background( MaterialTheme.colorScheme.primary)
        )
        Text(
            text = stringResource(R.string.free),
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(Modifier.width(5.dp))
        Box(
            Modifier
                .clip(Shapes.medium)
                .size(20.dp)
                .background( Gray500)
        )
        Text(
            text = stringResource(R.string.taken),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
    IconTextButton(
        icon = Icons.Filled.Add,
        text = stringResource(R.string.reserve),
        color = Gray500,
        onClick = {}
    )
}