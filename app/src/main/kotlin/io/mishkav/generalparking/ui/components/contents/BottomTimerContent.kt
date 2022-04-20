package io.mishkav.generalparking.ui.components.contents

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
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
import io.mishkav.generalparking.ui.theme.*
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDateTime
import java.time.Period
import kotlin.math.abs

@Composable
fun BottomTimerScreen(
    name: String,
    navigateToSchemeScreen: () -> Unit
) {
    val viewModel: MapViewModel = viewModel()
    val currentParkingAddress by viewModel.currentParkingAddress.collectAsState()
    val period = 60 // in Minutes

    BottomTimerScreenContent(
        name = name,
        textAddress = currentParkingAddress,
        period = period,
        navigateToSchemeScreen = navigateToSchemeScreen
    )
}

@Composable
fun BottomTimerScreenContent(
    name: String = stringResource(R.string.zeros),
    textAddress: String = stringResource(R.string.bottom_title),
    textCost: String = stringResource(R.string.minute_cost),
    period: Int = 60,
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
            period = period
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
    period: Int
) {
    //reservedTime is taken from DB
    var reservedTime = LocalDateTime.of(2022,4,20,23,30)

    reservedTime = reservedTime.plusMinutes(period.toLong())
    var diffMin = Duration.between(LocalDateTime.now(), reservedTime).toMinutesPart()
    var diffSec = abs(Duration.between(LocalDateTime.now(), reservedTime).toSecondsPart())

    var enabled by remember { mutableStateOf(true) }
    var progress by remember { mutableStateOf((diffMin*60+diffSec).toFloat().div(period*60)) }
    var currTime by remember { mutableStateOf("$diffMin:$diffSec") }

    val animatedProgress by animateFloatAsState(
        targetValue = progress
    )

    LaunchedEffect(enabled) {
        while ((progress > 0) && enabled || diffMin>-60) {
            diffMin = Duration.between(LocalDateTime.now(), reservedTime).toMinutesPart()
            diffSec = abs(Duration.between(LocalDateTime.now(), reservedTime).toSecondsPart())
            currTime = "$diffMin:$diffSec"
            if (enabled)
                progress = (diffMin*60+diffSec).toFloat().div(period*60)
            delay((1000).toLong())
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
            .padding(horizontal = dimensionResource(R.dimen.bottom_padding))
    ) {
        Text(
            text = stringResource(R.string.reservation_retention),
            color = MaterialTheme.colorScheme.onPrimary,
            style = Typography.button
        )
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        dimensionResource(R.dimen.bottom_shape),
                    )
                )
                .background(Gray200)
        ) {
            Text(
                text = currTime,
                color = generalParkingDarkBackground,
                style = Typography.body1,
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.standard_padding))
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBottomTimerScreen() {
    BottomTimerScreenContent(navigateToSchemeScreen = {})
}