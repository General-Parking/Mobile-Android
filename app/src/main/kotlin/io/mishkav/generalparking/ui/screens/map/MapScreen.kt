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
import io.mishkav.generalparking.ui.screens.scheme.SchemeViewModel
import io.mishkav.generalparking.ui.screens.scheme.components.ParkingSchemeConsts
import io.mishkav.generalparking.ui.theme.Shapes
import io.mishkav.generalparking.ui.utils.ErrorResult
import io.mishkav.generalparking.ui.utils.LoadingResult
import io.mishkav.generalparking.ui.utils.SuccessResult
import io.mishkav.generalparking.ui.utils.getGoogleMapStyleOption

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
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        viewModel.resetIsArrived()
                                        navController.navigate(Routes.map)
                                    }
                                ) {
                                    Text(
                                        text = stringResource(R.string.continue_text)
                                    )
                                }
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
