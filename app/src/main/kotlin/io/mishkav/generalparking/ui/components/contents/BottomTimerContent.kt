package io.mishkav.generalparking.ui.components.contents

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.buttons.IconTextButton
import io.mishkav.generalparking.ui.components.buttons.SimpleIconButton
import io.mishkav.generalparking.ui.components.buttons.SimpleIconTextButton
import io.mishkav.generalparking.ui.components.errors.OnErrorResult
import io.mishkav.generalparking.ui.components.loaders.CircularLoader
import io.mishkav.generalparking.ui.components.texts.BottomBody
import io.mishkav.generalparking.ui.components.texts.BottomTitle
import io.mishkav.generalparking.ui.screens.map.MapViewModel
import io.mishkav.generalparking.ui.theme.*
import io.mishkav.generalparking.ui.utils.ErrorResult
import io.mishkav.generalparking.ui.utils.LoadingResult
import io.mishkav.generalparking.ui.utils.SuccessResult
import kotlinx.coroutines.delay
import java.time.*
import java.time.format.DateTimeFormatter
import kotlin.math.abs

@Composable
fun BottomTimerScreen(
    name: String,
    navController: NavHostController,
    navigateToSchemeScreen: () -> Unit
) {
    val viewModel: MapViewModel = viewModel()
    val currentParkingAddress by viewModel.currentParkingAddress.collectAsState()
    val timeReservationResult by viewModel.timeReservationResult.collectAsState()
    val bookingTimeResult by viewModel.bookingTimeResult.collectAsState()
    val timeReservation by viewModel.timeReservation

    timeReservationResult.also { result ->
        when (result) {
            is ErrorResult -> OnErrorResult(
                onClick = {
                    viewModel.onOpen()
                },
                message = result.message ?: R.string.on_error_def,
                navController = navController,
                isTopAppBarAvailable = true
            )
            is SuccessResult -> bookingTimeResult.also { bookingResult ->
                when (bookingResult) {
                    is ErrorResult -> OnErrorResult(
                        onClick = {
                            viewModel.onOpen()
                        },
                        message = bookingResult.message ?: R.string.on_error_def,
                        navController = navController,
                        isTopAppBarAvailable = true
                    )
                    is SuccessResult -> BottomTimerScreenContent(
                        name = name,
                        textAddress = currentParkingAddress,
                        period = bookingTimeResult.data!!,
                        navigateToSchemeScreen = navigateToSchemeScreen,
                        timeReservationResult = timeReservation
                    )
                    is LoadingResult -> {
                        Box(
                            modifier = Modifier
                                .alpha(0.5f)
                                .background(MaterialTheme.colorScheme.background)
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularLoader()
                        }
                    }
                }
            }
            is LoadingResult -> Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularLoader()
            }
        }
    }
}

@Composable
fun BottomTimerScreenContent(
    modifier: Modifier = Modifier,
    name: String = stringResource(R.string.zeros),
    textAddress: String = stringResource(R.string.bottom_title),
    textCost: String = stringResource(R.string.minute_cost),
    period: Long,
    timeReservationResult: String,
    navigateToSchemeScreen: () -> Unit = {}
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
            period = period,
            timeReservationResult = timeReservationResult
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
    period: Long,
    timeReservationResult: String,
    darkTheme: Boolean = isSystemInDarkTheme()
) {

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
    var timeReservation = LocalDateTime.parse(timeReservationResult, formatter)

    timeReservation = timeReservation.plusMinutes(period)
    var diffH = abs(Duration.between(LocalDateTime.parse(LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of("Atlantic/Reykjavik")).format(formatter), formatter), timeReservation).toHoursPart())
    var diffMin = abs(Duration.between(LocalDateTime.parse(LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of("Atlantic/Reykjavik")).format(formatter), formatter), timeReservation).toMinutesPart())
    var diffSec = abs(Duration.between(LocalDateTime.parse(LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of("Atlantic/Reykjavik")).format(formatter), formatter), timeReservation).toSecondsPart())

    var enabled by remember { mutableStateOf(true) }
    var progress by remember {
        mutableStateOf(
            (diffMin * 60 + diffSec).toFloat().div(period.toInt() * 60)
        )
    }
    if (Duration.between(LocalDateTime.parse(LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of("Atlantic/Reykjavik")).format(formatter), formatter), timeReservation).isNegative) {
        enabled = false
        progress = 0f
    }
    var currTime by remember {
        mutableStateOf(
            when (diffH) {
                0 -> String.format("%02d:%02d", diffMin, diffSec)
                else -> String.format("%02d:%02d:%02d", diffH, diffMin, diffSec)
            }
        )
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress
    )

    LaunchedEffect(enabled) {
        while (!(Duration.between(LocalDateTime.parse(LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of("Atlantic/Reykjavik")).format(formatter), formatter),timeReservation).isNegative) &&
            enabled || (Duration.between(LocalDateTime.parse(LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of("Atlantic/Reykjavik")).format(formatter), formatter), timeReservation)
                .toMinutesPart() > -60)
        ) {
            diffH = abs(Duration.between(LocalDateTime.parse(LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of("Atlantic/Reykjavik")).format(formatter), formatter), timeReservation).toHoursPart())
            diffMin = abs(Duration.between(LocalDateTime.parse(LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of("Atlantic/Reykjavik")).format(formatter), formatter), timeReservation).toMinutesPart())
            diffSec = abs(Duration.between(LocalDateTime.parse(LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of("Atlantic/Reykjavik")).format(formatter), formatter), timeReservation).toSecondsPart())
            currTime = when (diffH) {
                0 -> String.format("%02d:%02d", diffMin, diffSec)
                else -> String.format("%02d:%02d:%02d", diffH, diffMin, diffSec)
            }
            if (enabled)
                progress = (diffMin * 60 + diffSec).toFloat().div(period.toInt() * 60)
            delay((1000).toLong())
        }
    }

    if (Duration.between(LocalDateTime.parse(LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of("Atlantic/Reykjavik")).format(formatter), formatter), timeReservation).isNegative) {
        enabled = false
    }

    LinearProgressIndicator(
        modifier = Modifier
            .fillMaxSize(),
        progress = animatedProgress,
        backgroundColor = when {
            !enabled && darkTheme -> Orange400
            !enabled && !darkTheme -> Gray400
            else -> MaterialTheme.colorScheme.background
        },
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
            text = when {
                enabled -> stringResource(R.string.reservation_retention)
                else -> stringResource(R.string.paid_retention)
            },
            color = when {
                enabled -> MaterialTheme.colorScheme.onPrimary
                else -> generalParkingLightBackground
            },
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
    BottomTimerScreenContent(
        navigateToSchemeScreen = {},
        timeReservationResult = "",
        period = 60
    )
}