package com.egoriku.animatedbottomsheet.bottomsheet.collapsed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import io.mishkav.generalparking.R

@Composable
fun SheetCollapsed(
    currentFraction: Float,
    content: @Composable RowScope.() -> Unit
) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.scheme_horizontal_padding))
                .height(72.dp)
                .background(Color.Transparent)
                .graphicsLayer(alpha = 1f - currentFraction),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            content()
        }
}