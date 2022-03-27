package io.mishkav.generalparking.ui.screens.scheme.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import io.mishkav.generalparking.R

@Composable
fun SchemeCardView(
    modifier: Modifier = Modifier
        .padding(
            horizontal = dimensionResource(R.dimen.scheme_horizontal_in_padding)
        ),
    content: @Composable () -> Unit
) = Card(
    elevation = 8.dp,
    shape = RoundedCornerShape(8.dp),
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary),
    backgroundColor = MaterialTheme.colorScheme.background,
    modifier = modifier,
    content = content
)