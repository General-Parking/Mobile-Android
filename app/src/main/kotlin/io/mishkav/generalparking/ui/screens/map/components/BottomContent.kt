package io.mishkav.generalparking.ui.components.contents

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapCalls
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.buttons.BottomTextButton
import io.mishkav.generalparking.ui.components.buttons.SimpleIconTextButton
import io.mishkav.generalparking.ui.components.texts.BottomBody
import io.mishkav.generalparking.ui.components.texts.BottomTitle
import io.mishkav.generalparking.ui.screens.map.MapViewModel

@Composable
fun BottomScreen(
    navigateToSchemeScreen: () -> Unit
) {
    val viewModel: MapViewModel = viewModel()
    val currentParkingAddress by viewModel.currentParkingAddress.collectAsState()
    val parkingShortInfoResult by viewModel.parkingShortInfoResult.collectAsState()

    val currentParkingInfo = parkingShortInfoResult.data?.get(currentParkingAddress)
    BottomScreenContent(
        textAddress = currentParkingAddress,
        freePlaces = currentParkingInfo?.freePlaces ?: 0,
        totalPlaces = currentParkingInfo?.totalPlaces ?: 0,
        priceParking = currentParkingInfo?.priceOfParking ?: 0f,
        navigateToSchemeScreen = navigateToSchemeScreen
    )
}

@Composable
fun BottomScreenContent(
    textAddress: String = stringResource(R.string.bottom_title),
    freePlaces: Int = 0,
    totalPlaces: Int = 0,
    priceParking: Float = 0f,
    navigateToSchemeScreen: () -> Unit = {},
    modifier: Modifier = Modifier
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
        text = stringResource(R.string.bottom_body).format(freePlaces, totalPlaces)
    )

    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
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
            text = stringResource(R.string.minute_cost).format(priceParking),
            color = MaterialTheme.colorScheme.onPrimary
        )
        SimpleIconTextButton(
            icon = Icons.Outlined.Info,
            text = stringResource(R.string.more),
            onClick = {}
        )
    }
    BottomTextButton(
        onClick = navigateToSchemeScreen
    )
}