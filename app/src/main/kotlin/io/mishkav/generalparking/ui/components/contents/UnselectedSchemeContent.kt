package io.mishkav.generalparking.ui.components.contents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.texts.BottomTitle
import io.mishkav.generalparking.ui.theme.Gray500
import io.mishkav.generalparking.ui.theme.Shapes

@Composable
fun UnselectedSchemeContent(
) = Column(
    modifier = Modifier
        .padding(
            vertical = dimensionResource(R.dimen.bottom_padding),
            horizontal = dimensionResource(R.dimen.bottom_padding)
        )
        .background(Color.Transparent)
        .fillMaxWidth()
        .wrapContentHeight(),
    verticalArrangement = Arrangement.Center
) {
    BottomTitle(
        text = stringResource(R.string.choose_seat)
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        Box(
            Modifier
                .clip(Shapes.medium)
                .size(20.dp)
                .background(MaterialTheme.colorScheme.primary)
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
                .background(Gray500)
        )
        Text(
            text = stringResource(R.string.taken),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

