package io.mishkav.generalparking.ui.screens.scheme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
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
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material3.IconButton
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.egoriku.animatedbottomsheet.bottomsheet.collapsed.SheetCollapsed
import com.egoriku.animatedbottomsheet.bottomsheet.expanded.SheetExpanded
import com.google.accompanist.pager.ExperimentalPagerApi
import io.mishkav.generalparking.ui.components.SelectedSchemeContent
import io.mishkav.generalparking.ui.components.UnselectedSchemeContent
import io.mishkav.generalparking.ui.components.topAppBar.TopAppBarWithBackButton
import io.mishkav.generalparking.ui.components.zoomable.Zoomable
import io.mishkav.generalparking.ui.components.zoomable.rememberZoomableState
import io.mishkav.generalparking.ui.screens.scheme.components.EmptyLotTile
import io.mishkav.generalparking.ui.screens.scheme.components.NotSelectedPlaceState
import io.mishkav.generalparking.ui.screens.scheme.components.ParkingLotTile
import io.mishkav.generalparking.ui.screens.scheme.components.ParkingPlaceStateColor
import io.mishkav.generalparking.ui.screens.scheme.components.SchemeState
import io.mishkav.generalparking.ui.screens.scheme.components.SelectedPlaceState
import io.mishkav.generalparking.ui.screens.scheme.components.bottomsheet.extension.currentFraction
import kotlinx.coroutines.launch

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

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun SchemeScreenContent(
    textAddress: String = stringResource(R.string.bottom_title),
    parking: Map<String, ParkingScheme>,
    parkingState: SchemeState,
    onParkingPlaceClick: (state: SchemeState) -> Unit = { _ -> },
    navigateBack: () -> Unit = {},
) {
    val scope = rememberCoroutineScope()

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(BottomSheetValue.Collapsed)
    )

    val sheetToggle: () -> Unit = {
        scope.launch {
            if (scaffoldState.bottomSheetState.isCollapsed) {
                scaffoldState.bottomSheetState.expand()
            } else {
                scaffoldState.bottomSheetState.collapse()
            }
        }
    }
    val sheetToggleExpanded: () -> Unit = {
        scope.launch {
            scaffoldState.bottomSheetState.expand()
        }
    }
    val sheetToggleCollapse: () -> Unit = {
        scope.launch {
            scaffoldState.bottomSheetState.collapse()
        }
    }

    val sheetPeekHeight = 72.dp
    val emptyOnClick: () -> Unit = {}
    val onClick: (state: SchemeState) -> Unit = { state ->
        when {
            parkingState is NotSelectedPlaceState && scaffoldState.bottomSheetState.isCollapsed -> sheetToggleExpanded()
            parkingState is SelectedPlaceState && parkingState.coordinates != state.coordinates -> sheetToggleExpanded()
            parkingState is SelectedPlaceState && parkingState.coordinates == state.coordinates -> sheetToggleCollapse()
        }
        onParkingPlaceClick(state)
    }

    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        sheetBackgroundColor = Color.Transparent,
        topBar = {
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
        },
        sheetContent = {
            SheetCollapsed(
                currentFraction = scaffoldState.currentFraction,
            ) {
                IconButton(
                    onClick = if (scaffoldState.bottomSheetState.isCollapsed) sheetToggle else emptyOnClick,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_info),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
            SheetExpanded {
                when (parkingState) {
                    is NotSelectedPlaceState -> UnselectedSchemeContent()
                    is SelectedPlaceState -> SelectedSchemeContent(name = parkingState.name)
                }
            }
        },
        sheetElevation = 0.dp,
        sheetPeekHeight = sheetPeekHeight
    ) {
        DrawScheme(
            parkingScheme = parking[parking.keys.elementAt(1)]!!,
            parkingState = parkingState,
            onParkingPlaceClick = onClick,
        )
    }
}

private fun getBackgroundColor(
    state: SchemeState,
    coordinates: String,
    placeSelected: Int
): Color =  when{
    state.coordinates == coordinates -> state.colorState.color
    placeSelected == 1 -> ParkingPlaceStateColor.RESERVED_BY_OTHER_USERS.color
    else -> ParkingPlaceStateColor.NOT_SELECTED.color
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun DrawScheme(
    parkingScheme: ParkingScheme,
    parkingState: SchemeState,
    onParkingPlaceClick: (state: SchemeState) -> Unit = { _ -> }
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
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
                                        modifier = Modifier
                                            // .aspectRatio(1f)
                                            .weight(1f)
                                    )
                                    else -> ParkingLotTile(
                                        modifier = Modifier
                                            // .aspectRatio(1f)
                                            .weight(1f)
                                            .padding(1.dp),
                                        parkingPlace = currentPlace,
                                        coordinates = "${height}_${width}",
                                        background = getBackgroundColor(parkingState, coordinates, currentPlace.value),
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
            parkingState = NotSelectedPlaceState()
        )
    }
}