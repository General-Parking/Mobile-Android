package io.mishkav.generalparking.domain.entities

data class ParkingScheme(
    val width: Int,
    val height: Int,
    val places: Map<String, ParkingPlace>
) {
    companion object {
        fun getInstance(
            width: Int = 0,
            height: Int = 0,
            places: Map<String, ParkingPlace> = emptyMap()
        ): ParkingScheme = ParkingScheme(
            width = width,
            height = height,
            places = places
        )
    }
}

data class ParkingPlace(
    val name: String,
    val rotation: Int,
    var value: Int
)

data class ParkingShortInfo(
    val freePlaces: Int,
    val priceOfParking: Float,
    val totalPlaces: Int
)