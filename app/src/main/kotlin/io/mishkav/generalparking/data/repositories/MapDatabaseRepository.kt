package io.mishkav.generalparking.data.repositories

import com.google.firebase.database.DatabaseReference
import io.mishkav.generalparking.domain.entities.ParkingPlace
import io.mishkav.generalparking.domain.entities.ParkingScheme
import io.mishkav.generalparking.domain.repositories.IMapDatabaseRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MapDatabaseRepository @Inject constructor(
    private val firebaseDatabase: DatabaseReference
) : IMapDatabaseRepository {

    override suspend fun getParkingCoordinates(): Map<String, String> {
        return firebaseDatabase
            .child(PATH_TO_PARKING_COORDINATES)
            .get()
            .await()
            .getValue() as Map<String, String>
    }

    override suspend fun getParkingScheme(address: String, floor: Int): ParkingScheme {
        val rawScheme = firebaseDatabase
            .child("parking/$address/$floor")
            .get()
            .await()

        val width = rawScheme.child(PATH_TO_WIDTH).getValue(CLASS_INT)!!
        val height = rawScheme.child(PATH_TO_HEIGHT).getValue(CLASS_INT)!!
        val places = hashMapOf<String, ParkingPlace>()
        for (place in rawScheme.child(PATH_TO_PLACES).children) {
            val name = place.child(PATH_TO_PLACE_NAME).getValue(String::class.java)!!
            val rotation = place.child(PATH_TO_PLACE_ROTATE).getValue(Int::class.java)!!
            val value = place.child(PATH_TO_PLACE_VALUE).getValue(Int::class.java)!!

            places[place.key!!] = ParkingPlace(
                name = name,
                rotation = rotation,
                value = value
            )
        }

        return ParkingScheme(
            width = width,
            height = height,
            places = places
        )
    }

    companion object {
        private const val PATH_TO_PARKING_COORDINATES = "parking/coordinates_address"
        private const val PATH_TO_WIDTH = "m"
        private const val PATH_TO_HEIGHT = "n"
        private const val PATH_TO_PLACES = "places"
        private const val PATH_TO_PLACE_NAME = "name"
        private const val PATH_TO_PLACE_ROTATE = "rotate"
        private const val PATH_TO_PLACE_VALUE = "value"

        private val CLASS_STRING = String::class.java
        private val CLASS_INT = Int::class.java
    }
}