package io.mishkav.generalparking.ui.screens.map.schemeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.insets.systemBarsPadding
import io.mishkav.generalparking.R
import io.mishkav.generalparking.domain.entities.ParkingScheme
import io.mishkav.generalparking.ui.components.UnselectedSchemeContent
import io.mishkav.generalparking.ui.components.loaders.CircularLoader
import io.mishkav.generalparking.ui.components.texts.BottomBody
import io.mishkav.generalparking.ui.components.texts.BottomTitle
import io.mishkav.generalparking.ui.screens.map.schemeScreen.components.EmptyLotTile
import io.mishkav.generalparking.ui.screens.map.schemeScreen.components.ParkingLotTile
import io.mishkav.generalparking.ui.theme.GeneralParkingTheme
import io.mishkav.generalparking.ui.utils.ErrorResult
import io.mishkav.generalparking.ui.utils.LoadingResult
import io.mishkav.generalparking.ui.utils.SuccessResult
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import io.mishkav.generalparking.ui.screens.map.schemeScreen.components.ParkingSchemeSizes.BASE_TILE_SIZE
import io.mishkav.generalparking.ui.screens.map.schemeScreen.components.ParkingTile

@Composable
fun SchemeScreen(
    navController: NavHostController,
    onError: @Composable (Int) -> Unit
) {
    val viewModel: SchemeViewModel = viewModel()
    val currentParkingAddress by viewModel.currentParkingAddress.collectAsState()
    val parkingSchemeResult by viewModel.parkingSchemeResult.collectAsState()

    //Что-то придумать с floor
    LaunchedEffect(Unit) {
        viewModel.getParkingScheme(-1)
    }

    parkingSchemeResult.also { result ->
        when (result) {
            is ErrorResult -> onError(result.message!!)
            is SuccessResult -> {
                parkingSchemeResult.data?.let {
                    SchemeScreenContent(
                        textAddress = currentParkingAddress,
                        parkingScheme = it
                    )
                }
            }
            is LoadingResult -> {
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
    }
}

@Composable
fun SchemeScreenContent(
    textAddress: String = stringResource(R.string.bottom_title),
    parkingScheme: ParkingScheme
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(
                vertical = dimensionResource(R.dimen.bottom_padding)
            ),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(2f)
                .padding(
                    horizontal = dimensionResource(R.dimen.bottom_padding)
                )
        ) {
            BottomTitle(
                text = textAddress
            )

            Spacer(modifier = Modifier.height(16.dp))

            BottomBody(
                text = stringResource(R.string.parking_scheme)
            )
        }

        Column(
            modifier = Modifier
                .weight(7f),
            verticalArrangement = Arrangement.Center
        ) {

            DrawScheme(
                parkingScheme = parkingScheme
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    horizontal = dimensionResource(R.dimen.bottom_padding)
                ),
            verticalArrangement = Arrangement.Bottom
        ) {
            UnselectedSchemeContent()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DrawScheme(
    parkingScheme: ParkingScheme
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        offset += offsetChange
    }

    Box(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .verticalScroll(rememberScrollState())
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y
            )
            .transformable(state = state)
            .width(((parkingScheme.width) * BASE_TILE_SIZE.value).dp)
            .height(((parkingScheme.height) * BASE_TILE_SIZE.value).dp)
    ) {
        Column {
            for (height in 1..parkingScheme.height + 1) {
                Row {
                    for (width in 1..parkingScheme.width + 1) {
                        when (parkingScheme.places["${height}_${width}"]) {
                            null -> EmptyLotTile()
                            else -> parkingScheme.places["${height}_${width}"]?.let {
                                ParkingLotTile(
                                    parkingTile = ParkingTile(it)
                                )
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
            parkingScheme = ParkingScheme.getInstance()
        )
    }
}