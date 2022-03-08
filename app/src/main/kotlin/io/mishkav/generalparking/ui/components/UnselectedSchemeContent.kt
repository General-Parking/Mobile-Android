package io.mishkav.generalparking.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.SwapCalls
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.buttons.IconTextButton
import io.mishkav.generalparking.ui.components.buttons.SimpleIconTextButton
import io.mishkav.generalparking.ui.components.texts.BottomTitle
import io.mishkav.generalparking.ui.theme.Gray500

@Composable
fun UnselectedSchemeContent(
) = Column(
    modifier = Modifier
        .padding(
            horizontal = dimensionResource(R.dimen.bottom_padding),
            vertical = dimensionResource(R.dimen.bottom_top_padding)
        )
) {
    BottomTitle(
        text = stringResource(R.string.choose_seat)
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Text(
            text = stringResource(R.string.free),
            color = MaterialTheme.colorScheme.onPrimary
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
    SimpleIconTextButton(
        icon = Icons.Filled.SwapCalls,
        text = stringResource(R.string.route),
        color = MaterialTheme.colorScheme.primary,
        onClick = {}
    )
}