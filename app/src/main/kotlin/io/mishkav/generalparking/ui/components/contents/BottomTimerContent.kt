package io.mishkav.generalparking.ui.components.contents

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.SwapCalls
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.buttons.IconTextButton
import io.mishkav.generalparking.ui.components.buttons.SimpleIconButton
import io.mishkav.generalparking.ui.components.buttons.SimpleIconTextButton
import io.mishkav.generalparking.ui.components.texts.BottomBody
import io.mishkav.generalparking.ui.components.texts.BottomTitle
import io.mishkav.generalparking.ui.screens.map.MapViewModel
import io.mishkav.generalparking.ui.theme.Gray500
import io.mishkav.generalparking.ui.theme.Green600
import io.mishkav.generalparking.ui.theme.Typography
import kotlinx.coroutines.delay

@Composable
fun BottomTimerScreen(
    name: String,
    navigateToSchemeScreen: () -> Unit
) {
    val viewModel: MapViewModel = viewModel()
    val currentParkingAddress by viewModel.currentParkingAddress.collectAsState()
    val reservedTime = 1800000 // in Millis

    BottomTimerScreenContent(
        name = name,
        textAddress = currentParkingAddress,
        reservedTime = reservedTime,
        navigateToSchemeScreen = navigateToSchemeScreen
    )
}

@Composable
fun BottomTimerScreenContent(
    name: String = stringResource(R.string.zeros),
    textAddress: String = stringResource(R.string.bottom_title),
    textCost: String = stringResource(R.string.minute_cost),
    reservedTime: Int = 1800000,
    navigateToSchemeScreen: () -> Unit = {},
    modifier: Modifier = Modifier
) = Column(
    modifier = Modifier
        .fillMaxWidth()
) {
    Surface(
        elevation = dimensionResource(R.dimen.standard_elevation),
        shape = RoundedCornerShape(
            dimensionResource(R.dimen.bottom_shape)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.standard_padding))
            .height(dimensionResource(R.dimen.timer_height))
    ) {
        TimerBar(
            reservedTime = reservedTime
        )
    }
    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    dimensionResource(R.dimen.bottom_shape),
                    dimensionResource(R.dimen.bottom_shape),
                    dimensionResource(R.dimen.null_dp),
                    dimensionResource(R.dimen.null_dp)
                )
            )
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = dimensionResource(R.dimen.bottom_padding),
                    vertical = dimensionResource(R.dimen.bottom_top_padding)
                )
        ) {
            BottomTitle(
                text = textAddress
            )

            BottomBody(
                text = "${stringResource(R.string.chosen_seat)} $name"
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.standard_padding))
                    .horizontalScroll(rememberScrollState())
            ) {
                SimpleIconTextButton(
                    icon = Icons.Filled.SwapCalls,
                    text = stringResource(R.string.route),
                    color = MaterialTheme.colorScheme.primary,
                    onClick = {}
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = textCost,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                SimpleIconTextButton(
                    icon = Icons.Outlined.Info,
                    text = stringResource(R.string.more),
                    onClick = {}
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconTextButton(
                    icon = Icons.Filled.Done,
                    text = stringResource(R.string.reserved),
                    color = Green600,
                    onClick = navigateToSchemeScreen
                )
                Spacer(Modifier.width(5.dp))
                SimpleIconButton(
                    icon = Icons.Filled.Delete,
                    color = Gray500,
                    onClick = {}
                )
            }
        }
    }
}

@Composable
fun TimerBar(
    reservedTime: Int
) {
    var enabled by remember { mutableStateOf(true) }
    var progress by remember { mutableStateOf(1f) }
    var currTime by remember { mutableStateOf(reservedTime) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress
    )

    LaunchedEffect(enabled) {
        while ((progress > 0) && enabled) {
            progress -= 0.001f
            currTime -= 1000
            delay((reservedTime/1000).toLong())
        }
    }

    if (progress <= 0f) {
        enabled = false
    }

    LinearProgressIndicator(
        modifier = Modifier
            .fillMaxSize(),
        progress = animatedProgress,
        backgroundColor = MaterialTheme.colorScheme.background,
        color = Green600
    )

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.standard_padding))
    ) {
        Text(
            text = "Удержание брони:",
            color = MaterialTheme.colorScheme.onPrimary,
            style = Typography.button
        )
        Text(
            text = "?",
            color = MaterialTheme.colorScheme.onPrimary,
            style = Typography.body1
        )
        Text(
            text = "$currTime",
            color = MaterialTheme.colorScheme.onPrimary,
            style = Typography.body1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBottomTimerScreen() {
    BottomTimerScreenContent(navigateToSchemeScreen = {})
}