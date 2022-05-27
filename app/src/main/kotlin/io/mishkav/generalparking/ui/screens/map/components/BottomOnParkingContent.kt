package io.mishkav.generalparking.ui.screens.map.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.mishkav.generalparking.R
import io.mishkav.generalparking.domain.entities.TIME_ZONE
import io.mishkav.generalparking.domain.entities.UserState
import io.mishkav.generalparking.ui.components.buttons.IconTextButton
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
fun BottomOnParkingScreen(
    name: String,
    navController: NavHostController,
    navigateToSchemeScreen: () -> Unit
) {
    val viewModel: MapViewModel = viewModel()
    val currentParkingAddress by viewModel.currentParkingAddress.collectAsState()
    val timeArriveResult by viewModel.timeArriveResult.collectAsState()
    val timeArrive by viewModel.timeArrive
    val isExit by viewModel.isExit.collectAsState()
    val parkingShortInfoResult by viewModel.parkingShortInfoResult.collectAsState()
    val currentParkingInfo = parkingShortInfoResult.data?.get(currentParkingAddress)

    timeArriveResult.also { result ->
        when (result) {
            is ErrorResult -> OnErrorResult(
                onClick = {
                    viewModel.onOpen()
                },
                message = result.message ?: R.string.on_error_def,
                navController = navController,
                isTopAppBarAvailable = true
            )
            is SuccessResult -> BottomOnParkingScreenContent(
                name = name,
                textAddress = currentParkingAddress,
                priceParking = currentParkingInfo?.priceOfParking ?: 0f,
                navigateToSchemeScreen = navigateToSchemeScreen,
                isExit = isExit,
                timeArriveResult = timeArrive
            )
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
fun BottomOnParkingScreenContent(
    name: String = stringResource(R.string.zeros),
    textAddress: String = stringResource(R.string.bottom_title),
    priceParking: Float = 0f,
    timeArriveResult: String,
    isExit: String = UserState.NOTHING.value,
    navigateToSchemeScreen: () -> Unit = {}
) = Column(
    modifier = Modifier
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

    OnParkingBar(
        timeArriveResult = timeArriveResult,
        priceParking = priceParking,
        isExit = isExit,
        navigateToSchemeScreen = navigateToSchemeScreen
    )
}

@Composable
fun OnParkingBar(
    timeArriveResult: String,
    priceParking: Float = 0f,
    isExit: String,
    darkTheme: Boolean = isSystemInDarkTheme(),
    navigateToSchemeScreen: () -> Unit = {}
) {

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
    val timeArrive = LocalDateTime.parse(timeArriveResult, formatter)

    var difference = Duration.between(
        LocalDateTime.parse(
            LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of(TIME_ZONE))
                .format(formatter), formatter
        ), timeArrive
    )
    var differenceHours = abs(difference.toHours().toInt() % 24)
    var differenceMinutes = abs(difference.seconds.toInt() % (60 * 60) / 60)
    var differenceSeconds = abs(difference.seconds.toInt() % 60)

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

    var currentPrice by remember {
        mutableStateOf(
            (priceParking / 60).toInt() * differenceMinutes
        )
    }

    LaunchedEffect(isExit) {
        while (isExit != UserState.EXIT.value) {
            difference = Duration.between(
                LocalDateTime.parse(
                    LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of(TIME_ZONE))
                        .format(formatter), formatter
                ), timeArrive
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
            currentPrice = (priceParking / 60).toInt() * (differenceHours * 60 + differenceMinutes)
            delay(1000L)
        }
    }

    Spacer(Modifier.height(5.dp))

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        IconTextButton(
            modifier = Modifier
                .height(42.dp),
            icon = Icons.Filled.HourglassEmpty,
            text = stringResource(R.string.time_on_parking),
            color = Color.White,
            enabled = false,
            tint = MaterialTheme.colorScheme.onPrimary,
            onClick = {}
        )

        Spacer(Modifier.width(5.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        dimensionResource(R.dimen.bottom_shape),
                    )
                )
                .background(Gray200)
                .height(42.dp)
        ) {
            Text(
                text = currentTime,
                color = generalParkingDarkBackground,
                style = Typography.body1,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.standard_padding))
            )
        }
    }

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.standard_padding))
            .horizontalScroll(rememberScrollState())
    ) {
        SimpleIconTextButton(
            icon = Icons.Filled.ZoomOutMap,
            text = stringResource(R.string.scheme),
            color = MaterialTheme.colorScheme.primary,
            onClick = navigateToSchemeScreen
        )

        Spacer(Modifier.width(dimensionResource(R.dimen.standard_padding)))

        Row(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        dimensionResource(R.dimen.bottom_shape),
                    )
                )
                .background(
                    when {
                        darkTheme -> generalParkingLightBackground
                        else -> Gray400
                    }
                )
                .padding(dimensionResource(R.dimen.standard_padding))
        ) {
            Icon(
                imageVector = Icons.Filled.CreditCard,
                contentDescription = stringResource(R.string.space),
                tint = MaterialTheme.colorScheme.background
            )
            Text(
                text = "$currentPrice ₽",
                color = MaterialTheme.colorScheme.background,
                style = Typography.body1,
                modifier = Modifier
                    .padding(start = dimensionResource(R.dimen.half_standard_padding))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomOnParkingScreenPreview() {
    BottomOnParkingScreenContent(
        navigateToSchemeScreen = {},
        timeArriveResult = ""
    )
}