package io.mishkav.generalparking.data.repositories

import com.google.firebase.database.DatabaseReference
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

    companion object {
        private const val PATH_TO_PARKING_COORDINATES = "parking/coordinates_address"

        private val CLASS_STRING = String::class.java
        private val CLASS_INT = Int::class.java
    }
}