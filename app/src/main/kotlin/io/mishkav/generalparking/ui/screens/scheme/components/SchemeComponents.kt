package io.mishkav.generalparking.ui.screens.scheme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.mishkav.generalparking.domain.entities.ParkingPlace
import io.mishkav.generalparking.ui.screens.scheme.AutoSizeText
import io.mishkav.generalparking.ui.theme.Gray500
import io.mishkav.generalparking.ui.theme.Orange400
import io.mishkav.generalparking.ui.theme.Yellow400

@Composable
private fun BaseTile(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {},
) = Box(
    modifier = modifier,
    contentAlignment = Alignment.CenterStart,
    content = content
)

@Composable
fun EmptyLotTile(
    modifier: Modifier = Modifier
) = BaseTile(modifier = modifier)

@Composable
fun ParkingLotTile(
    modifier: Modifier = Modifier,
    parkingPlace: ParkingPlace,
    background: Color,
    coordinates: String,
    onClick: (state: SchemeState) -> Unit = { _ -> }
) = BaseTile(
    modifier = modifier
) {
    // TODO Card -> Box Если хотим использовать aspectRotation
    Card(
        modifier = Modifier
            // .fillMaxHeight(if (parkingPlace.rotation == 0) 0.5f else 1f)
            // .fillMaxWidth(if (parkingPlace.rotation == 0) 1f else 0.5f)
            .fillMaxSize()
            .clickable {
                if (background != ParkingPlaceStateColor.RESERVED_BY_OTHER_USERS.color &&
                    background != ParkingPlaceStateColor.RESERVED_BY_CURRENT_USER.color
                ) {
                    onClick(
                        SelectedPlaceState(
                            name = parkingPlace.name,
                            coordinates = coordinates
                        )
                    )
                }
            }
    ) {
        AutoSizeText(
            text = parkingPlace.name,
            modifier = Modifier
                .background(background)
                .fillMaxSize()
                .padding(0.5.dp),
            color = MaterialTheme.colorScheme.onPrimary,
            maxLines = 1
        )
    }
}

enum class ParkingPlaceStateColor(val color: Color) {
    NOT_SELECTED(Yellow400),
    SELECTED(Orange400),
    RESERVED_BY_CURRENT_USER(Color.Green),
    RESERVED_BY_OTHER_USERS(Gray500)
}

sealed class SchemeState(
    val colorState: ParkingPlaceStateColor,
    val name: String = "",
    val coordinates: String = "",
)

class NotSelectedPlaceState : SchemeState(
    colorState = ParkingPlaceStateColor.NOT_SELECTED
)

class SelectedPlaceState(
    name: String,
    coordinates: String
) : SchemeState(
    colorState = ParkingPlaceStateColor.SELECTED,
    name = name,
    coordinates = coordinates
)

class ReservedPlaceState(
    name: String,
    coordinates: String
) : SchemeState(
    colorState = ParkingPlaceStateColor.RESERVED_BY_CURRENT_USER,
    name = name,
    coordinates = coordinates
)
