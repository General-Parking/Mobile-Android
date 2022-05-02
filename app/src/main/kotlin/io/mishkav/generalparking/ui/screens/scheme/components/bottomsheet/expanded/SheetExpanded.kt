package com.egoriku.animatedbottomsheet.bottomsheet.expanded

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import io.mishkav.generalparking.R

@Composable
fun SheetExpanded(
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .padding(
                horizontal = dimensionResource(R.dimen.scheme_horizontal_padding)
            )
            .fillMaxWidth()
            .fillMaxHeight(0.18f)
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(MaterialTheme.colorScheme.background)
    ) {
        content()
    }
}