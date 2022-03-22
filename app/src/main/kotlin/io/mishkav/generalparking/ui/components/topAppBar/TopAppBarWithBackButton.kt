package io.mishkav.generalparking.ui.components.topAppBar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import io.mishkav.generalparking.R

@Composable
fun TopAppBarWithBackButton(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    navigateBack: (() -> Unit)? = null
) = SmallTopAppBar(
    title = title,
    navigationIcon = navigateBack?.let {
        {
            IconButton(onClick = it) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    } ?: {},
    actions = actions,
    modifier = modifier.statusBarsPadding()
)