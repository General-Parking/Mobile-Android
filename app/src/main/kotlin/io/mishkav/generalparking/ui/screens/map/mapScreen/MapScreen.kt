package io.mishkav.generalparking.ui.screens.map.mapScreen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapCalls
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.buttons.BottomTextButton
import io.mishkav.generalparking.ui.components.buttons.IconTextButton
import io.mishkav.generalparking.ui.components.texts.BottomBody
import io.mishkav.generalparking.ui.components.texts.BottomTitle
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    navController: NavHostController,
    onError: @Composable (Int) -> Unit
) {

}

@Composable
fun BottomContent(
    textTitle: String = stringResource(R.string.bottom_title),
    textBody: String = stringResource(R.string.bottom_body),
    textCost: String = stringResource(R.string.minute_cost)
) = Column(
    modifier = Modifier
        .padding(
            horizontal = dimensionResource(R.dimen.bottom_padding),
            vertical = dimensionResource(R.dimen.bottom_top_padding)
        )
) {
    BottomTitle(
        text = textTitle
    )
    BottomBody(
        text = textBody
    )
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        IconTextButton(
            icon = Icons.Filled.SwapCalls,
            text = stringResource(R.string.route),
            color = MaterialTheme.colorScheme.primary,
            onClick = {}
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = textCost,
            color = MaterialTheme.colorScheme.onPrimary
        )
        IconTextButton(
            icon = Icons.Outlined.Info,
            text = stringResource(R.string.more),
            onClick = {}
        )
    }
    BottomTextButton(
        onClick = {}
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapScreenContent(
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold(
        sheetShape = RoundedCornerShape(
            dimensionResource(R.dimen.bottom_shape), dimensionResource(R.dimen.bottom_shape),
            dimensionResource(R.dimen.null_dp), dimensionResource(R.dimen.null_dp)
        ),
        scaffoldState = bottomSheetScaffoldState,
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        sheetContent = { BottomContent() },
        sheetPeekHeight = dimensionResource(R.dimen.null_dp)
    ) {
        val singapore = LatLng(1.35, 103.87)
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            googleMapOptionsFactory = {
                GoogleMapOptions().camera(CameraPosition.fromLatLngZoom(singapore, 11f))
            }
        ) {
            val markerClick: (Marker) -> Boolean = {
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
                position = singapore,
                title = "Singapore",
                snippet = "Marker in Singapore",
                onClick = markerClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMapScreen() {
    MapScreenContent()
}