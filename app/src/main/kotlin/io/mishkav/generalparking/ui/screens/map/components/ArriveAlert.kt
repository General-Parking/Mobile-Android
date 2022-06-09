package io.mishkav.generalparking.ui.screens.map.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.buttons.TextButton
import io.mishkav.generalparking.ui.theme.*

@Composable
fun ArriveAlert(
    onClick: () -> Unit = {}
) = AlertDialog(
    shape = RoundedCornerShape(
        dimensionResource(R.dimen.bottom_shape),
    ),
    onDismissRequest = {},
    text = {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.standard_padding))
            ) {
                Text(
                    text = stringResource(R.string.arrive_title),
                    style = Typography.h5,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.standard_padding))
            ) {
                Text(
                    text = stringResource(R.string.arrive_body),
                    style = Typography.h6,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    },
    backgroundColor = MaterialTheme.colorScheme.background,
    buttons = {
        Row(
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.exit_button_padding))
                .offset(y = (-15).dp),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(
                    dimensionResource(R.dimen.bottom_shape),
                ),
                text = stringResource(R.string.continue_text),
                onClick = onClick
            )
        }
    }
)