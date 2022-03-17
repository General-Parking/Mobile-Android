package io.mishkav.generalparking.ui.screens.map.schemeScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.mishkav.generalparking.domain.entities.ParkingPlace
import io.mishkav.generalparking.ui.screens.map.schemeScreen.components.ParkingSchemeSizes.BASE_TILE_PADDING
import io.mishkav.generalparking.ui.screens.map.schemeScreen.components.ParkingSchemeSizes.BASE_TILE_SIZE
import io.mishkav.generalparking.ui.screens.map.schemeScreen.components.ParkingSchemeSizes.PARKING_LOT_HEIGHT
import io.mishkav.generalparking.ui.screens.map.schemeScreen.components.ParkingSchemeSizes.PARKING_LOT_WIDTH
import io.mishkav.generalparking.ui.theme.Typography
import io.mishkav.generalparking.ui.theme.Yellow400
import kotlinx.coroutines.flow.MutableStateFlow

sealed interface Tile

class ParkingTile(
    val place: ParkingPlace,
    var isOccuerd: Boolean = false
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
fun EmptyLotTile() = BaseTile() {}

@Composable
fun ParkingLotTile(
    parkingTile: ParkingTile,
    onClick: () -> Unit = {}
) = BaseTile {

    val background = remember {
        mutableStateOf(Yellow400)
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

    Text(
        modifier = Modifier
            .width(width)
            .height(height)
            .background(background.value)
            .clickable(onClick = {
                if (parkingTile.isOccuerd)
                    background.value = Color.Green
                else
                    background.value= Yellow400
                parkingTile.isOccuerd = !parkingTile.isOccuerd
            }),
        text = parkingTile.place.name,
        style = Typography.body1,
        color = MaterialTheme.colorScheme.onPrimary
    )
}

object ParkingSchemeSizes {
    val BASE_TILE_SIZE = 100.dp
    val BASE_TILE_PADDING = 5.dp
    val PARKING_LOT_WIDTH = 100.dp
    val PARKING_LOT_HEIGHT = 70.dp
}