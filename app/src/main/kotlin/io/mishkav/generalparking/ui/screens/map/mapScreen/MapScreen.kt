package io.mishkav.generalparking.ui.screens.map.mapScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapCalls
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
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
import io.mishkav.generalparking.ui.theme.BottomColor
import io.mishkav.generalparking.ui.theme.Shapes
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapScreen(
    navController: NavHostController,
    onError: @Composable (Int) -> Unit
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold(
        sheetShape = Shapes.small,
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .height(240.dp)
                    .padding(dimensionResource(R.dimen.bottom_padding))
            ) {
                BottomTitle(
                    text = "Улица"
                )
                BottomBody(
                    text = "Свободно ... мест из ..."
                )
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    IconTextButton(
                        icon = Icons.Filled.SwapCalls,
                        text = "Маршрут",
                        color = BottomColor,
                        onClick = {}
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = ".. р/мин"
                    )
                    IconTextButton(
                        icon = Icons.Outlined.Info,
                        text = "Подробнее",
                        onClick = {}
                    )
                }
                BottomTextButton(
                    text = "Перейти к бронированию",
                    onClick = {}
                )
            }
        }, sheetPeekHeight = 0.dp
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