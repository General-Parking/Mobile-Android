package io.mishkav.generalparking.ui.screens.map.schemeScreen.components

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    isGivenPlaceSelected: (name: String) -> Boolean = { _ -> false }
) = BaseTile {

    val isOccurred = remember {
        mutableStateOf(false)
    }
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
                    if (isOccurred.value)
                        onClick(EMPTY_STRING, EMPTY_STRING)
                    else
                        onClick(parkingTile.place.name, coordinates)
                    isOccurred.value = !isOccurred.value
                }
            }),
        shape = RoundedCornerShape(8.dp),
        elevation = 8.dp
    ) {
        Text(
            modifier = Modifier
                .background(if (isOccurred.value || isGivenPlaceSelected(parkingTile.place.name)) Color.Green else Yellow400)
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

object ParkingSchemeConsts {
    val BASE_TILE_SIZE = 100.dp
    val BASE_TILE_PADDING = 5.dp
    val PARKING_LOT_WIDTH = 100.dp
    val PARKING_LOT_HEIGHT = 70.dp
    val EMPTY_STRING = ""
}