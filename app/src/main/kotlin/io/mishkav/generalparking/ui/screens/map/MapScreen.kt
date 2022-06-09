package io.mishkav.generalparking.ui.screens.map

import android.graphics.Bitmap
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.screens.main.Routes
import kotlinx.coroutines.launch
import com.google.maps.android.compose.rememberCameraPositionState
import io.mishkav.generalparking.domain.entities.UserState
import io.mishkav.generalparking.ui.screens.map.components.BottomScreen
import io.mishkav.generalparking.ui.components.loaders.CircularLoader
import io.mishkav.generalparking.ui.components.errors.OnErrorResult
import io.mishkav.generalparking.ui.components.texts.ScreenBody
import io.mishkav.generalparking.ui.components.buttons.TextButton
import io.mishkav.generalparking.ui.screens.map.components.*
import io.mishkav.generalparking.ui.theme.*
import io.mishkav.generalparking.ui.utils.*

@Composable
fun MapScreen(
    navController: NavHostController,
    showMessage: (message: Int) -> Unit = {}
) {
    val viewModel: MapViewModel = viewModel()
    val parkingCoordinates by viewModel.parkingCoordinatesResult.collectAsState()
    parkingCoordinates.subscribeOnErrorMax { id ->
        OnErrorResult(
            onClick = viewModel::onOpen,
            message = id,
            navController = navController,
            isTopAppBarAvailable = false
        )
    }
    val autoNumber by viewModel.autoNumberResult.collectAsState()
    autoNumber.subscribeOnError(showMessage)

    val userState by viewModel.userState.collectAsState()
    val alertState by viewModel.alertState.collectAsState()
    val isAlert by viewModel.isAlertResult.collectAsState()
    isAlert.subscribeOnErrorMax { id ->
        OnErrorResult(
            onClick = viewModel::onOpen,
            message = id,
            navController = navController,
            isTopAppBarAvailable = false
        )
    }
    val resetAlert by viewModel.resetAlertResult.collectAsState()
    resetAlert.subscribeOnError(showMessage)
    val isMinSdkVersionApproved by viewModel.isMinSdkVersionApproved.collectAsState()

    val timeArrive by viewModel.timeArrive.collectAsState()
    val timeReservation by viewModel.timeReservation.collectAsState()
    val timeExit by viewModel.timeExitResult.collectAsState()
    timeExit.subscribeOnErrorMax { id ->
        OnErrorResult(
            onClick = viewModel::onOpen,
            message = id,
            navController = navController,
            isTopAppBarAvailable = false
        )
    }
    val bookingRatio by viewModel.bookingRatioResult.collectAsState()
    bookingRatio.subscribeOnErrorMax { id ->
        OnErrorResult(
            onClick = viewModel::onOpen,
            message = id,
            navController = navController,
            isTopAppBarAvailable = false
        )
    }
    val selectedParkingPlace by viewModel.selectedParkingPlace.collectAsState()
    val reservationAddress by viewModel.reservationAddressResult.collectAsState()
    reservationAddress.subscribeOnErrorMax { id ->
        OnErrorResult(
            onClick = viewModel::onOpen,
            message = id,
            navController = navController,
            isTopAppBarAvailable = true
        )
    }
    var openArriveDialog by remember { mutableStateOf(true) }
    val currentParkingAddress by viewModel.currentParkingAddress.collectAsState()
    val parkingShortInfoResult by viewModel.parkingShortInfoResult.collectAsState()
    val currentParkingInfo = parkingShortInfoResult.data?.get(currentParkingAddress)

    LaunchedEffect(Unit) { viewModel.onOpen() }
    isMinSdkVersionApproved.takeIf { it is SuccessResult }?.data?.let { result ->
        LaunchedEffect(Unit) {
            if (!result) {
                navController.navigate(Routes.authorization)
            }
        }
    }

    val isLoading = parkingCoordinates is LoadingResult || reservationAddress is LoadingResult
            || isAlert is LoadingResult || timeExit is LoadingResult || bookingRatio is LoadingResult

    MapScreenContent(
        parkingCoordinates = parkingCoordinates.data ?: emptyMap(),
        selectedParkingPlace = selectedParkingPlace,
        userState = userState,
        navController = navController,
        reservationAddress = reservationAddress.data ?: "",
        isLoading = isLoading,
        setParkingAddress = viewModel::setCurrentParkingAddress,
        navigateToSchemeScreen = {
            navController.navigate(Routes.scheme)
        },
        navigateToProfileScreen = {
            navController.navigate(Routes.profile)
        }
    )

    if (alertState == UserState.ARRIVED && openArriveDialog) {
        ArriveAlert(
            onClick = {
                viewModel.resetAlertState(UserState.ARRIVED)
                openArriveDialog = false
            }
        )
    }
    else if (alertState == UserState.EXIT) {
        if (timeExit.data ?: "" != "") {
            ExitAlert(
                timeExitResult = timeExit.data ?: "",
                timeArriveResult = timeArrive,
                timeReservationResult = timeReservation,
                priceParking = currentParkingInfo?.priceOfParking ?: 0f,
                bookingRatio = bookingRatio.data ?: 0.2,
                onClick = {
                    viewModel.resetAlertState(UserState.EXIT)
                    viewModel.removeParkingPlaceReservation()
                    navController.navigate(Routes.map)
                }
            )
        }
        else {
            viewModel.getTimeExit()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapScreenContent(
    parkingCoordinates: Map<Pair<Double, Double>, String> = emptyMap(),
    selectedParkingPlace: String = "",
    userState: UserState = UserState.NOT_RESERVED,
    navController: NavHostController,
    reservationAddress: String,
    isLoading: Boolean = false,
    setParkingAddress: (address: String) -> Unit = { _ -> },
    navigateToSchemeScreen: () -> Unit = {},
    navigateToProfileScreen: () -> Unit = {}
) {
    val moscowLatLng = LatLng(Coordinates.Moscow.latitude, Coordinates.Moscow.longitude)
    val cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(moscowLatLng, 11f)
    }
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = when (userState) {
            UserState.NOT_RESERVED -> BottomSheetState(BottomSheetValue.Collapsed)
            else -> BottomSheetState(BottomSheetValue.Expanded)
        }
    )
    val bottomSheetGesturesEnabled = remember {
        mutableStateOf(
            when (userState) {
                UserState.NOT_RESERVED-> true
                else -> false
            }
        )
    }

    val coroutineScope = rememberCoroutineScope()

    val rawImage = ImageBitmap.imageResource(id = R.drawable.ic_marker).asAndroidBitmap()
    val image = Bitmap.createScaledBitmap(rawImage, 130, 170, false)
    var alertChangeParking by remember { mutableStateOf(false) }
    var showAlertChangeParking by remember { mutableStateOf(false) }

    if (isLoading) {
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

    BottomSheetScaffold(
        sheetShape = RoundedCornerShape(
            dimensionResource(R.dimen.bottom_shape),
            dimensionResource(R.dimen.bottom_shape),
            dimensionResource(R.dimen.null_dp),
            dimensionResource(R.dimen.null_dp)
        ),
        sheetElevation = dimensionResource(R.dimen.null_dp),
        scaffoldState = bottomSheetScaffoldState,
        sheetGesturesEnabled = bottomSheetGesturesEnabled.value,
        sheetBackgroundColor = when (userState) {
            UserState.RESERVED -> Color.Transparent
            else -> MaterialTheme.colorScheme.background
        },
        sheetContent = {

            Spacer(modifier = Modifier.height(1.dp)) //After a re-compose the sheetContent looses associated anchor

            when (userState) {
                UserState.RESERVED -> if (!alertChangeParking) {
                    BottomTimerScreen(
                        name = selectedParkingPlace,
                        navigateToSchemeScreen = navigateToSchemeScreen
                    )
                } else if (showAlertChangeParking) {
                    AlertDialog(
                        onDismissRequest = {},
                        text = {
                            ScreenBody(
                                text = stringResource(R.string.parking_change_body)
                            )
                        },
                        backgroundColor = MaterialTheme.colorScheme.background,
                        buttons = {
                            Row(
                                modifier = Modifier.padding(all = 8.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                TextButton(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(R.string.good_text),
                                    onClick = {
                                        showAlertChangeParking = false
                                    }
                                )
                            }
                        }
                    )
                }
                UserState.ARRIVED -> if (!alertChangeParking) {
                    BottomOnParkingScreen(
                        name = selectedParkingPlace,
                        navController = navController,
                        navigateToSchemeScreen = navigateToSchemeScreen
                    )
                } else if (showAlertChangeParking) {
                    AlertDialog(
                        onDismissRequest = {},
                        text = {
                            ScreenBody(
                                text = stringResource(R.string.parking_change_body)
                            )
                        },
                        backgroundColor = MaterialTheme.colorScheme.background,
                        buttons = {
                            Row(
                                modifier = Modifier.padding(all = 8.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                TextButton(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(R.string.good_text),
                                    onClick = {
                                        showAlertChangeParking = false
                                    }
                                )
                            }
                        }
                    )
                }
                else -> {
                    BottomScreen(
                        navigateToSchemeScreen = navigateToSchemeScreen
                    )
                }
            }
        },
        sheetPeekHeight = dimensionResource(R.dimen.null_dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            GoogleMap(
                modifier = Modifier,
                // TODO Добавить темную тему
                // properties = MapProperties(
                //     mapStyleOptions = if (isSystemInDarkTheme()) getGoogleMapStyleOption() else null
                // ),
                cameraPositionState = cameraPosition
            ) {
                for ((coordinates, address) in parkingCoordinates) {
                    val parkingLatLng = LatLng(coordinates.first, coordinates.second)
                    val markerClick: (Marker) -> Boolean = {
                        setParkingAddress(address)
                        coroutineScope.launch {
                            if (reservationAddress.isEmpty() || reservationAddress == address) {
                                alertChangeParking = false
                                if (userState == UserState.NOT_RESERVED) {
                                    bottomSheetGesturesEnabled.value = true
                                    if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                        bottomSheetScaffoldState.bottomSheetState.expand()
                                    } else {
                                        bottomSheetScaffoldState.bottomSheetState.collapse()
                                    }
                                }
                                else {
                                    bottomSheetGesturesEnabled.value = false
                                    bottomSheetScaffoldState.bottomSheetState.expand()
                                }
                            } else {
                                alertChangeParking = true
                                showAlertChangeParking = true
                            }
                        }
                        false
                    }
                    Marker(
                        position = parkingLatLng,
                        icon = BitmapDescriptorFactory.fromBitmap(image),
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
