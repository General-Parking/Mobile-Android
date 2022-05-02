package io.mishkav.generalparking.ui.screens.map

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.screens.main.Routes
import kotlinx.coroutines.launch
import com.google.maps.android.compose.rememberCameraPositionState
import io.mishkav.generalparking.ui.components.contents.*
import io.mishkav.generalparking.ui.components.loaders.CircularLoader
import io.mishkav.generalparking.ui.components.errors.OnErrorResult
import io.mishkav.generalparking.ui.components.texts.ScreenBody
import io.mishkav.generalparking.ui.components.texts.ScreenTitle
import io.mishkav.generalparking.ui.components.buttons.TextButton
import io.mishkav.generalparking.ui.screens.scheme.SchemeViewModel
import io.mishkav.generalparking.ui.screens.scheme.components.ParkingSchemeConsts
import io.mishkav.generalparking.ui.theme.Gray200
import io.mishkav.generalparking.ui.theme.Shapes
import io.mishkav.generalparking.ui.theme.Typography
import io.mishkav.generalparking.ui.theme.generalParkingDarkBackground
import io.mishkav.generalparking.ui.utils.ErrorResult
import io.mishkav.generalparking.ui.utils.LoadingResult
import io.mishkav.generalparking.ui.utils.SuccessResult
import io.mishkav.generalparking.ui.utils.getGoogleMapStyleOption
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

@Composable
fun MapScreen(
    navController: NavHostController,
    onError: @Composable (Int) -> Unit
) {
    val viewModel: MapViewModel = viewModel()
    val parkingCoordinates by viewModel.parkingCoordinatesResult.collectAsState()
    val autoNumber by viewModel.autoNumberResult.collectAsState()
    val userState by viewModel.userState.collectAsState()

    val isArrived by viewModel.isArrived.collectAsState()
    val isArrivedResult by viewModel.isArrivedResult.collectAsState()
    val isExit by viewModel.isExit.collectAsState()
    val isExitResult by viewModel.isExitResult.collectAsState()

    val timeArrive by viewModel.timeArrive
    val timeReservation by viewModel.timeReservation
    val timeExitResult by viewModel.timeExitResult.collectAsState()
    val priceParkingResult by viewModel.priceParkingResult.collectAsState()
    val bookingRatioResult by viewModel.bookingRatioResult.collectAsState()

    val schemeViewModel: SchemeViewModel = viewModel()
    val selectedParkingPlace by schemeViewModel.selectedParkingPlace.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onOpen()
        schemeViewModel.onOpenMap()
    }

    parkingCoordinates.also { result ->
        when (result) {
            is ErrorResult -> OnErrorResult(
                onClick = {
                    viewModel.onOpen()
                    schemeViewModel.onOpenMap()
                },
                message = result.message ?: R.string.on_error_def,
                navController = navController,
                isTopAppBarAvailable = false
            )
            is SuccessResult -> when (autoNumber) {
                is ErrorResult -> onError(result.message!!)
                else -> {
                    MapScreenContent(
                        parkingCoordinates = parkingCoordinates.data ?: emptyMap(),
                        selectedParkingPlace = selectedParkingPlace,
                        userState = userState,
                        navController = navController,
                        setParkingAddress = viewModel::setCurrentParkingAddress,
                        navigateToSchemeScreen = {
                            navController.navigate(Routes.scheme)
                        },
                        navigateToProfileScreen = {
                            navController.navigate(Routes.profile)
                        }
                    )
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

    isArrivedResult.also { result ->
        when (result) {
            is ErrorResult -> OnErrorResult(
                onClick = {
                    viewModel.onOpen()
                },
                message = result.message ?: R.string.on_error_def,
                navController = navController,
                isTopAppBarAvailable = false
            )
            is SuccessResult ->
                if (isArrived == "arrived")
                    AlertDialog(
                        onDismissRequest = {},
                        text = {
                            Column {
                                ScreenTitle(
                                    text = stringResource(R.string.arrive_title)
                                )
                                ScreenBody(
                                    text = stringResource(R.string.arrive_body)
                                )
                            }
                        },
                        backgroundColor = MaterialTheme.colorScheme.background,
                        buttons = {
                            Row(
                                modifier = Modifier.padding(all = 8.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                TextButton(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(R.string.continue_text),
                                    onClick = {
                                        viewModel.resetIsArrived()
                                        navController.navigate(Routes.map)
                                    }
                                )
                            }
                        }
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

    isExitResult.also { result ->
        when (result) {
            is ErrorResult -> OnErrorResult(
                onClick = {
                    viewModel.onOpen()
                },
                message = result.message ?: R.string.on_error_def,
                navController = navController,
                isTopAppBarAvailable = false
            )
            is SuccessResult ->
                if (isExit == "exit")
                    timeExitResult.also { exitResult ->
                        when (exitResult) {
                            is ErrorResult -> OnErrorResult(
                                onClick = {
                                    viewModel.onOpen()
                                },
                                message = exitResult.message ?: R.string.on_error_def,
                                navController = navController,
                                isTopAppBarAvailable = false
                            )
                            is SuccessResult ->
                                bookingRatioResult.also { bookResult ->
                                when (bookResult) {
                                    is ErrorResult -> OnErrorResult(
                                        onClick = {
                                            viewModel.onOpen()
                                        },
                                        message = bookResult.message ?: R.string.on_error_def,
                                        navController = navController,
                                        isTopAppBarAvailable = false
                                    )
                                    is SuccessResult ->
                                        ExitAlert(
                                            timeExitResult = timeExitResult.data!!,
                                            timeArriveResult = timeArrive,
                                            timeReservationResult = timeReservation,
                                            priceParking = priceParkingResult.data!!,
                                            bookingRatio = bookingRatioResult.data!!,
                                            onClick = {
                                                schemeViewModel.removeParkingPlaceReservation(-1)
                                                viewModel.resetIsExit()
                                                navController.navigate(Routes.map)
                                            }
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapScreenContent(
    parkingCoordinates: Map<Pair<Double, Double>, String> = emptyMap(),
    selectedParkingPlace: String = ParkingSchemeConsts.EMPTY_STRING,
    userState: String = "",
    navController: NavHostController,
    setParkingAddress: (address: String) -> Unit = { _ -> },
    navigateToSchemeScreen: () -> Unit = {},
    navigateToProfileScreen: () -> Unit = {}
) {
    val moscowLatLng = LatLng(Coordinates.Moscow.latitude, Coordinates.Moscow.longitude)
    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(moscowLatLng, 11f)
    }
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()


    BottomSheetScaffold(
        sheetShape = RoundedCornerShape(
            dimensionResource(R.dimen.bottom_shape),
            dimensionResource(R.dimen.bottom_shape),
            dimensionResource(R.dimen.null_dp),
            dimensionResource(R.dimen.null_dp)
        ),
        sheetElevation = dimensionResource(R.dimen.null_dp),
        scaffoldState = bottomSheetScaffoldState,
        sheetBackgroundColor = when (userState) {
            "reserved" -> Color.Transparent
            else -> MaterialTheme.colorScheme.background
        },
        sheetContent = {
            when (userState) {
                "reserved" -> BottomTimerScreen(
                    name = selectedParkingPlace,
                    navController = navController,
                    navigateToSchemeScreen = navigateToSchemeScreen
                )
                "arrived" -> BottomOnParkingScreen(
                    name = selectedParkingPlace,
                    navController = navController,
                    navigateToSchemeScreen = navigateToSchemeScreen
                )
                else -> {
                    BottomScreen(
                        navigateToSchemeScreen = navigateToSchemeScreen
                    )
                }
            }
        },
        sheetPeekHeight = dimensionResource(R.dimen.null_dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier,
                properties = MapProperties(
                    mapStyleOptions = if (isSystemInDarkTheme()) getGoogleMapStyleOption() else null
                ),
                cameraPositionState = cameraPosition
            ) {
                for ((coordinates, address) in parkingCoordinates) {
                    val parkingLatLng = LatLng(coordinates.first, coordinates.second)
                    val markerClick: (Marker) -> Boolean = {
                        setParkingAddress(address)
                        coroutineScope.launch {
                            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            } else {
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                        }
                        false
                    }
                    Marker(
                        position = parkingLatLng,
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker),
                        onClick = markerClick
                    )
                }
            }
            Button(
                elevation = ButtonDefaults.elevation(6.dp, 8.dp),
                onClick = navigateToProfileScreen,
                shape = Shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .padding(
                        top = dimensionResource(R.dimen.fab_top),
                        start = dimensionResource(R.dimen.fab_start)
                    )
                    .size(dimensionResource(R.dimen.fab_size))
            ) {
                Icon(Icons.Filled.Menu, "")
            }
        }
    }
}

@Composable
fun ExitAlert(
    timeExitResult: String,
    timeArriveResult: String,
    timeReservationResult: String,
    priceParking: Long,
    bookingRatio: Double,
    onClick: () -> Unit = {}
) {

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
    var timeExit = LocalDateTime.parse(timeExitResult, formatter)
    var timeArrive = LocalDateTime.parse(timeArriveResult, formatter)
    var timeReservation = LocalDateTime.parse(timeReservationResult, formatter)

    var resultParkingPrice = (priceParking / 60).toInt() * abs(Duration.between(timeArrive, timeExit).toMinutes()).toInt()
    var resultRetentionPrice = ((priceParking / 60).toInt() * bookingRatio * abs(Duration.between(timeReservation, timeArrive).toMinutes())).toInt()

    AlertDialog(
        onDismissRequest = {},
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                ScreenTitle(
                    text = stringResource(R.string.see_you_again),
                    modifier = Modifier
                        .padding(vertical = dimensionResource(R.dimen.standard_padding))
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(vertical = dimensionResource(R.dimen.standard_padding))
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(
                                dimensionResource(R.dimen.bottom_shape),
                            )
                        )
                        .background(Gray200)
                        .padding(dimensionResource(R.dimen.standard_padding))
                ) {
                    Text(
                        text = stringResource(R.string.time_exit_alert),
                        color = generalParkingDarkBackground,
                        style = Typography.body1
                    )
                    Text(
                        text = "${abs(Duration.between(timeArrive, timeExit).toMinutes())} ${stringResource(R.string.minutes_exit_alert)}",
                        color = generalParkingDarkBackground,
                        style = Typography.body1
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(
                                dimensionResource(R.dimen.bottom_shape),
                            )
                        )
                        .background(Gray200)
                        .padding(dimensionResource(R.dimen.standard_padding))
                ) {
                    Text(
                        text = stringResource(R.string.price_exit_alert),
                        color = generalParkingDarkBackground,
                        style = Typography.body1
                    )
                    Text(
                        text = "$resultParkingPrice₽ + $resultRetentionPrice₽",
                        color = generalParkingDarkBackground,
                        style = Typography.body1
                    )
                    Spacer(Modifier.height(20.dp))
                }
            }
        },
        backgroundColor = MaterialTheme.colorScheme.background,
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.finish),
                    onClick = onClick
                )
            }
        }
    )
}
object Coordinates {
    object Moscow {
        const val longitude = 37.618423
        const val latitude = 55.751244
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMapScreen() {
}
