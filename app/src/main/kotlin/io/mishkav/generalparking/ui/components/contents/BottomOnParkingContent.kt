package io.mishkav.generalparking.ui.components.contents

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.mishkav.generalparking.R
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
import java.time.Duration
import java.time.LocalDateTime
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
                navigateToSchemeScreen = navigateToSchemeScreen,
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
    modifier: Modifier = Modifier,
    name: String = stringResource(R.string.zeros),
    textAddress: String = stringResource(R.string.bottom_title),
    textCost: String = stringResource(R.string.minute_cost),
    timeArriveResult: String,
    navigateToSchemeScreen: () -> Unit = {}
) = Column(
    modifier = modifier
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
            icon = Icons.Filled.HourglassEmpty,
            text = stringResource(R.string.time_on_parking),
            color = Color.White,
            tint = MaterialTheme.colorScheme.onPrimary,
            onClick = {}
        )
        Spacer(Modifier.width(5.dp))
        OnParkingBar(
            timeArriveResult = timeArriveResult
        )
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
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
}

@Composable
fun OnParkingBar(
    timeArriveResult: String
) {

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
    val timeArrive = LocalDateTime.parse(timeArriveResult, formatter)
    //val timeArrive = LocalDateTime.of(2022,4,20,14,30)
    var diffH = abs(Duration.between(LocalDateTime.now(), timeArrive).toHoursPart())
    var diffMin = abs(Duration.between(LocalDateTime.now(), timeArrive).toMinutesPart())
    var diffSec = abs(Duration.between(LocalDateTime.now(), timeArrive).toSecondsPart())

    var currTime by remember {
        mutableStateOf(
            when (diffH) {
                0 -> String.format("%02d:%02d", diffMin, diffSec)
                else -> String.format("%02d:%02d:%02d", diffH, diffMin, diffSec)
            }
        )
    }

    LaunchedEffect(Unit) {
        while (true) {
            diffH = abs(Duration.between(LocalDateTime.now(), timeArrive).toHoursPart())
            diffMin = abs(Duration.between(LocalDateTime.now(), timeArrive).toMinutesPart())
            diffSec = abs(Duration.between(LocalDateTime.now(), timeArrive).toSecondsPart())
            currTime = when (diffH) {
                0 -> String.format("%02d:%02d", diffMin, diffSec)
                else -> String.format("%02d:%02d:%02d", diffH, diffMin, diffSec)
            }
            delay((1000).toLong())
        }
    }

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

@Preview(showBackground = true)
@Composable
fun PreviewBottomOnParkingScreen() {
    BottomOnParkingScreenContent(
        navigateToSchemeScreen = {},
        timeArriveResult = ""
    )
}