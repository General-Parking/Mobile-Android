package io.mishkav.generalparking.ui.components.contents

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ZoomOutMap
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
                        period = bookingTimeResult.data ?: 60,
                        onRemoveReservationButtonClick = viewModel::removeParkingPlaceReservation,
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
    period: Long,
    timeReservationResult: String,
    onRemoveReservationButtonClick: () -> Unit = {},
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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                IconTextButton(
                    icon = Icons.Filled.Done,
                    text = stringResource(R.string.reserved),
                    color = Green600,
                    onClick = {}
                )
                Spacer(Modifier.width(5.dp))
                SimpleIconButton(
                    icon = Icons.Filled.Delete,
                    color = Gray500,
                    onClick = {
                        onRemoveReservationButtonClick()
                    }
                )
            }
            SimpleIconTextButton(
                icon = Icons.Filled.ZoomOutMap,
                text = stringResource(R.string.scheme),
                color = MaterialTheme.colorScheme.primary,
                onClick = navigateToSchemeScreen
            )
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

    var difference = Duration.between(
        LocalDateTime.parse(
            LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of("Atlantic/Reykjavik"))
                .format(formatter), formatter
        ), timeReservation
    )
    var differenceHours = abs(difference.toHours().toInt() % 24)
    var differenceMinutes = abs(difference.seconds.toInt() % (60 * 60) / 60)
    var differenceSeconds = abs(difference.seconds.toInt() % 60)

    var enabled by remember { mutableStateOf(true) }
    var progress by remember {
        mutableStateOf(
            (differenceMinutes * 60 + differenceSeconds).toFloat().div(period.toInt() * 60)
        )
    }
    if (Duration.between(
            LocalDateTime.parse(
                LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of("Atlantic/Reykjavik"))
                    .format(formatter), formatter
            ), timeReservation
        ).isNegative
    ) {
        enabled = false
        progress = 0f
    }
    var currentTime by remember {
        mutableStateOf(
            when (differenceHours) {
                0 -> String.format("%02d:%02d", differenceMinutes, differenceSeconds)
                else -> String.format(
                    "%02d:%02d:%02d",
                    differenceHours,
                    differenceMinutes,
                    differenceSeconds
                )
            }
        )
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progress
    )

    LaunchedEffect(enabled) {
        while (!(Duration.between(
                LocalDateTime.parse(
                    LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of("Atlantic/Reykjavik"))
                        .format(formatter), formatter
                ), timeReservation
            ).isNegative) &&
            enabled || (Duration.between(
                LocalDateTime.parse(
                    LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of("Atlantic/Reykjavik"))
                        .format(formatter), formatter
                ), timeReservation
            )
                .seconds.toInt() % (60 * 60) / 60 > -60)
        ) {
            difference = Duration.between(
                LocalDateTime.parse(
                    LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of("Atlantic/Reykjavik"))
                        .format(formatter), formatter
                ), timeReservation
            )
            differenceHours = abs(difference.toHours().toInt() % 24)
            differenceMinutes = abs(difference.seconds.toInt() % (60 * 60) / 60)
            differenceSeconds = abs(difference.seconds.toInt() % 60)

            currentTime = when (differenceHours) {
                0 -> String.format("%02d:%02d", differenceMinutes, differenceSeconds)
                else -> String.format(
                    "%02d:%02d:%02d",
                    differenceHours,
                    differenceMinutes,
                    differenceSeconds
                )
            }
            if (enabled)
                progress =
                    (differenceMinutes * 60 + differenceSeconds).toFloat().div(period.toInt() * 60)
            delay(1000L)
        }
    }

    if (Duration.between(
            LocalDateTime.parse(
                LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of("Atlantic/Reykjavik"))
                    .format(formatter), formatter
            ), timeReservation
        ).isNegative
    ) {
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
                text = currentTime,
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
fun BottomTimerScreenPreview() {
    BottomTimerScreenContent(
        timeReservationResult = "",
        period = 60
    )
}