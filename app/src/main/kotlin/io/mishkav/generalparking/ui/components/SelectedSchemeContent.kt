package io.mishkav.generalparking.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.buttons.IconTextButton
import io.mishkav.generalparking.ui.components.buttons.SimpleIconButton
import io.mishkav.generalparking.ui.components.texts.BottomTitle
import io.mishkav.generalparking.ui.theme.Gray500

@Composable
fun SelectedSchemeContent(
    name: String = stringResource(R.string.zeros),
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(
                top = dimensionResource(R.dimen.bottom_padding),
                start = dimensionResource(R.dimen.bottom_padding),
                end = dimensionResource(R.dimen.bottom_padding)
            )
            .fillMaxSize()
    ) {
        BottomTitle(
            text = "${stringResource(R.string.chosen_seat)} $name",
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.half_standard_padding))
        )
        Text(
            text = stringResource(R.string.floor_number),
            color = MaterialTheme.colorScheme.onPrimary
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.standard_padding))
        ) {
            IconTextButton(
                icon = Icons.Filled.Add,
                text = stringResource(R.string.reserve),
                color = MaterialTheme.colorScheme.primary,
                onClick = onClick
            )
            // Spacer(Modifier.width(dimensionResource(R.dimen.half_standard_padding)))
            // SimpleIconButton(
            //     icon = Icons.Filled.Close,
            //     color = Gray500,
            //     onClick = { }
            // )
        }
    }
}
