package io.mishkav.generalparking.ui.screens.scheme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.mishkav.generalparking.R
import io.mishkav.generalparking.domain.entities.ParkingScheme
import io.mishkav.generalparking.ui.components.loaders.CircularLoader
import io.mishkav.generalparking.ui.theme.GeneralParkingTheme
import io.mishkav.generalparking.ui.utils.ErrorResult
import io.mishkav.generalparking.ui.utils.LoadingResult
import io.mishkav.generalparking.ui.utils.SuccessResult
import androidx.compose.material.Text
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import io.mishkav.generalparking.ui.components.topAppBar.TopAppBarWithBackButton
import io.mishkav.generalparking.ui.components.zoomable.Zoomable
import io.mishkav.generalparking.ui.components.zoomable.rememberZoomableState
import io.mishkav.generalparking.ui.screens.scheme.components.EmptyLotTile
import io.mishkav.generalparking.ui.screens.scheme.components.NotSelectedState
import io.mishkav.generalparking.ui.screens.scheme.components.ParkingLotTile
import io.mishkav.generalparking.ui.screens.scheme.components.ParkingPlaceStateColor
import io.mishkav.generalparking.ui.screens.scheme.components.SchemeState

@Composable
fun SchemeScreen(
    navController: NavHostController,
    onError: @Composable (Int) -> Unit
) {
    val viewModel: SchemeViewModel = viewModel()
    val currentParkingAddress by viewModel.currentParkingAddress.collectAsState()
    val parkingSchemeResult by viewModel.parkingSchemeResult.collectAsState()
    val setParkingPlaceReservation by viewModel.setParkingPlaceReservationResult.collectAsState()
    val removeParkingPlaceReservation by viewModel.removeParkingPlaceReservationResult.collectAsState()
    val parkingState by viewModel.parkingSchemeState.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCurrentUser()
        viewModel.onOpen()
        viewModel.getParkingScheme()
    }

    when {
        currentUser is ErrorResult || parkingSchemeResult is ErrorResult -> onError(parkingSchemeResult.message!!)
        currentUser is SuccessResult && parkingSchemeResult is SuccessResult -> {
            parkingSchemeResult.data?.let {
                SchemeScreenContent(
                    textAddress = currentParkingAddress,
                    parking = it,
                    parkingState = parkingState,
                    onParkingPlaceClick = viewModel::setParkingSchemeState,
                    navigateBack = navController::popBackStack,
                )
            }
        }
        currentUser is LoadingResult || parkingSchemeResult is LoadingResult -> {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularLoader()
            }
        }
    }



    setParkingPlaceReservation.also { result ->
        when (result) {
            is ErrorResult -> onError(result.message!!)
            is SuccessResult -> {}
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

    removeParkingPlaceReservation.also { result ->
        when (result) {
            is ErrorResult -> onError(result.message!!)
            is SuccessResult -> {}
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
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun SchemeScreenContent(
    textAddress: String = stringResource(R.string.bottom_title),
    parking: Map<String, ParkingScheme>,
    parkingState: SchemeState,
    onParkingPlaceClick: (state: SchemeState) -> Unit = { _ -> },
    navigateBack: () -> Unit = {},
) = Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    TopAppBarWithBackButton(
        title = {
            Column {
                Text(
                    text = stringResource(R.string.parking_scheme),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = textAddress,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        navigateBack = navigateBack
    )

    DrawScheme(
        parkingScheme = parking[parking.keys.elementAt(1)]!!,
        parkingState = parkingState,
        onParkingPlaceClick = onParkingPlaceClick
    )
}

private fun getBackgroundColor(
    state: SchemeState,
    coordinates: String
): Color = if (state.coordinates == coordinates)
    state.colorState.color
else
    ParkingPlaceStateColor.NOT_SELECTED.color

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun DrawScheme(
    parkingScheme: ParkingScheme,
    parkingState: SchemeState,
    onParkingPlaceClick: (state: SchemeState) -> Unit = { _ -> },
) {
    LazyColumn {
        item {
            val zoomableState = rememberZoomableState(1f)
            Zoomable(
                state = zoomableState,
                modifier = Modifier.fillParentMaxHeight(1f),
                doubleTapScale = {
                    if (zoomableState.scale > 32f) {
                        zoomableState.minScale
                    } else {
                        zoomableState.scale * 1.5f
                    }
                }
            ) {
                Column {
                    for (height in 1..parkingScheme.height + 1) {
                        Row(
                            modifier = Modifier.weight(1f)
                        ) {
                            for (width in 1..parkingScheme.width + 1) {
                                val coordinates = "${height}_${width}"
                                val currentPlace = parkingScheme.places[coordinates]
                                when (currentPlace) {
                                    null -> EmptyLotTile(
                                        modifier = Modifier.weight(1f)
                                    )
                                    else -> ParkingLotTile(
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(1.dp),
                                        parkingPlace = currentPlace,
                                        coordinates = "${height}_${width}",
                                        background = getBackgroundColor(parkingState, coordinates),
                                        onClick = onParkingPlaceClick
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSchemeScreen() {

    GeneralParkingTheme {
        SchemeScreenContent(
            parking = mapOf("0" to ParkingScheme.getInstance()),
            parkingState = NotSelectedState()
        )
    }
}