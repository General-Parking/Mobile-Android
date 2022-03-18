package io.mishkav.generalparking.ui.screens.map.schemeScreen.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.mishkav.generalparking.domain.entities.ParkingPlace
import io.mishkav.generalparking.ui.screens.map.schemeScreen.components.ParkingSchemeConsts.BASE_TILE_PADDING
import io.mishkav.generalparking.ui.screens.map.schemeScreen.components.ParkingSchemeConsts.BASE_TILE_SIZE
import io.mishkav.generalparking.ui.screens.map.schemeScreen.components.ParkingSchemeConsts.EMPTY_STRING
import io.mishkav.generalparking.ui.screens.map.schemeScreen.components.ParkingSchemeConsts.PARKING_LOT_HEIGHT
import io.mishkav.generalparking.ui.screens.map.schemeScreen.components.ParkingSchemeConsts.PARKING_LOT_WIDTH
import io.mishkav.generalparking.ui.theme.Gray400
import io.mishkav.generalparking.ui.theme.Gray500
import io.mishkav.generalparking.ui.theme.Orange400
import io.mishkav.generalparking.ui.theme.Typography
import io.mishkav.generalparking.ui.theme.Yellow400

sealed interface Tile

class ParkingTile(
    val place: ParkingPlace,
    var isOccurred: Boolean = false
) : Tile

@Composable
private fun BaseTile(
    content: @Composable BoxScope.() -> Unit,
) = Box(
    modifier = Modifier
        .size(BASE_TILE_SIZE)
        .padding(BASE_TILE_PADDING),
    contentAlignment = Alignment.Center,
    content = content
)

@Composable
fun EmptyLotTile() = BaseTile {}

@Composable
fun ParkingLotTile(
    parkingTile: ParkingTile,
    coordinates: String,
    onGetSelectedParkingPlace: () -> String = { EMPTY_STRING },
    onClick: (name: String, coordinates: String) -> Unit = { _, _ -> },

    isGivenPlaceSelected: () -> Boolean = { false },

    getParkingPlaceState: (namePlace: String, value: Int) -> ParkingPlaceState = { _, _ -> ParkingPlaceState.NOT_RESERVED },
) = BaseTile {

    val state = getParkingPlaceState(parkingTile.place.name, parkingTile.place.value)
    var isReservedTransactionPassed = isGivenPlaceSelected()
    var isSelected by remember {
        mutableStateOf(false)
    }

    val background by animateColorAsState(
        when {
            state == ParkingPlaceState.RESERVED_BY_CURRENT_USER || state == ParkingPlaceState.RESERVED_BY_OTHER_USERS -> {
                state.color
            }
            isSelected -> {
                if (isReservedTransactionPassed) {
                    isSelected = false
                    isReservedTransactionPassed = false
                    state.color
                }
                else
                    ParkingPlaceState.SELECTED.color
            }
            else -> state.color
        }

        // if (isSelected) {
        //     ParkingPlaceState.SELECTED.color
        // }
        // else
        //     getParkingPlaceState(parkingTile.place.name, parkingTile.place.value).color
    )

    val width: Dp
    val height: Dp
    when (parkingTile.place.rotation) {
        0 -> {
            width = PARKING_LOT_WIDTH
            height = PARKING_LOT_HEIGHT
        }
        else -> {
            width = PARKING_LOT_HEIGHT
            height = PARKING_LOT_WIDTH
        }
    }

    Card(
        modifier = Modifier
            .width(width)
            .height(height)
            .clickable(onClick = {
                val selectedPlaceName = onGetSelectedParkingPlace()
                if (selectedPlaceName.isEmpty() || selectedPlaceName == parkingTile.place.name) {
                    if (isSelected)
                        onClick(EMPTY_STRING, EMPTY_STRING)
                    else
                        onClick(parkingTile.place.name, coordinates)
                    isSelected = !isSelected
                }
            }),
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp
    ) {
        Text(
            modifier = Modifier
                .background(background)
                .padding(
                    top = 20.dp
                )
                .fillMaxSize(),
            text = parkingTile.place.name,
            style = Typography.body1,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

enum class ParkingPlaceState(val color: Color) {
    NOT_RESERVED(Yellow400),
    SELECTED(Orange400),
    RESERVED_BY_CURRENT_USER(Color.Green),
    RESERVED_BY_OTHER_USERS(Gray500)
}

object ParkingSchemeConsts {
    val BASE_TILE_SIZE = 100.dp
    val BASE_TILE_PADDING = 5.dp
    val PARKING_LOT_WIDTH = 100.dp
    val PARKING_LOT_HEIGHT = 70.dp
    val EMPTY_STRING = ""
}