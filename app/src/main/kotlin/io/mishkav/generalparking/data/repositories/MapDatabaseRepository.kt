package io.mishkav.generalparking.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import io.mishkav.generalparking.data.exceptions.PlaceNotReservatedException
import io.mishkav.generalparking.data.exceptions.PlaceReservationException
import io.mishkav.generalparking.domain.entities.ParkingPlace
import io.mishkav.generalparking.domain.entities.ParkingScheme
import io.mishkav.generalparking.domain.entities.ParkingShortInfo
import io.mishkav.generalparking.domain.repositories.IMapDatabaseRepository
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class MapDatabaseRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: DatabaseReference
) : IMapDatabaseRepository {

    override suspend fun getParkingCoordinates(): Map<String, String> {
        return firebaseDatabase
            .child(PATH_TO_PARKING_COORDINATES)
            .get()
            .await()
            .getValue() as Map<String, String>
    }

    override suspend fun getParkingScheme(address: String): Map<String, ParkingScheme> {
        val rawScheme = firebaseDatabase
            .child("parking/$address")
            .get()
            .await()
        val parking: MutableMap<String, ParkingScheme> = mutableMapOf()

        for (floor in rawScheme.children.sortedByDescending { it.key?.toInt() }) {
            val width = rawScheme.child(floor.key!!).child(PATH_TO_WIDTH).getValue(CLASS_INT)!!
            val height = rawScheme.child(floor.key!!).child(PATH_TO_HEIGHT).getValue(CLASS_INT)!!
            val places = hashMapOf<String, ParkingPlace>()
            for (place in rawScheme.child(floor.key!!).child(PATH_TO_PLACES).children) {
                val name = place.child(PATH_TO_PLACE_NAME).getValue(CLASS_STRING)!!
                val rotation = place.child(PATH_TO_PLACE_ROTATE).getValue(CLASS_INT)!!
                val value = place.child(PATH_TO_PLACE_VALUE).getValue(CLASS_INT)!!

                places[place.key!!] = ParkingPlace(
                    name = name,
                    rotation = rotation,
                    value = value
                )
            }
            parking.put(
                floor.key!!,
                ParkingScheme(
                    width = width,
                    height = height,
                    places = places
                )
            )
        }
        return parking
    }

    override suspend fun setParkingPlaceReservation(
        address: String,
        namePlace: String,
        floor: String,
        placeCoordinates: String, //i_j
        autoNumber: String
    ) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.getDefault())
        val timeReservation = dateFormat.format(Date())

        //Check
        val checkReservation = firebaseDatabase
            .child("parking/$address/$floor/places/$placeCoordinates/value")
            .get()
            .await()
            .getValue() as Long

        if (checkReservation == 1L)
            throw PlaceReservationException()

        //Users car
        firebaseDatabase
            .child("users_car/$address/$autoNumber")
            .setValue(firebaseAuth.currentUser?.uid)
            .await()

        //Users
        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/reservation_address")
            .setValue(address)
            .await()

        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/reservation_place")
            .setValue(namePlace)
            .await()

        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/reservation_level")
            .setValue(floor)
            .await()

        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/time_reservation")
            .setValue(timeReservation)
            .await()

        //Parking
        firebaseDatabase
            .child("parking/$address/$floor/places/$placeCoordinates/reservation")
            .setValue(firebaseAuth.currentUser?.uid)
            .await()

        firebaseDatabase
            .child("parking/$address/$floor/places/$placeCoordinates/value")
            .setValue(1)
            .await()
    }

    override suspend fun removeParkingPlaceReservation(
        address: String,
        autoNumber: String,
        floor: Int,
        placeCoordinates: String
    ) {
        //Check
        val checkReservation = firebaseDatabase
            .child("parking/$address/$floor/places/$placeCoordinates/value")
            .get()
            .await()
            .getValue() as Long

        if (checkReservation == 0L)
            throw PlaceNotReservatedException()

        //Users
        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/reservation_address")
            .setValue(EMPTY_STRING)
            .await()

        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/reservation_place")
            .setValue(EMPTY_STRING)
            .await()

        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/reservation_level")
            .setValue(EMPTY_STRING)
            .await()

        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/time_reservation")
            .setValue(EMPTY_STRING)
            .await()

        //Parking
        firebaseDatabase
            .child("parking/$address/$floor/places/$placeCoordinates/reservation")
            .setValue(SPACE)
            .await()

        firebaseDatabase
            .child("parking/$address/$floor/places/$placeCoordinates/value")
            .setValue(0)
            .await()

        //Users car
        firebaseDatabase
            .child("users_car/$address/$autoNumber")
            .removeValue()
            .await()
    }

    override suspend fun getAutoNumber(): String {
        return firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/number_auto")
            .get()
            .await()
            .getValue() as String
    }

    override suspend fun getParkingShortInfo(): Map<String, ParkingShortInfo> {
        val rawData = firebaseDatabase
            .child(PATH_TO_PARKING_SHORT_INFO)
            .get()
            .await()
        var resultMap = mutableMapOf<String, ParkingShortInfo>()

        for (item in rawData.children) {
            resultMap[item.key!!] = ParkingShortInfo(
                freePlaces = item.child(PATH_TO_FREE_PLACES).getValue(CLASS_INT)!!,
                priceOfParking = item.child(PATH_TO_PRICE_PARKING).getValue(CLASS_FLOAT)!!,
                totalPlaces = item.child(PATH_TO_TOTAL_PLACES).getValue(CLASS_INT)!!
            )
        }

        return resultMap
    }

    companion object {
        private const val PATH_TO_PARKING_COORDINATES = "parking/coordinates_address"
        private const val PATH_TO_WIDTH = "m"
        private const val PATH_TO_HEIGHT = "n"
        private const val PATH_TO_PLACES = "places"
        private const val PATH_TO_PLACE_NAME = "name"
        private const val PATH_TO_PLACE_ROTATE = "rotate"
        private const val PATH_TO_PLACE_VALUE = "value"

        // Short Info
        private const val PATH_TO_PARKING_SHORT_INFO = "parking_short_info"
        private const val PATH_TO_FREE_PLACES = "free_places"
        private const val PATH_TO_PRICE_PARKING = "price_parking"
        private const val PATH_TO_TOTAL_PLACES = "total_places"

        private const val EMPTY_STRING = ""
        private const val SPACE = " "

        private val CLASS_STRING = String::class.java
        private val CLASS_INT = Int::class.java
        private val CLASS_FLOAT = Float::class.java
    }
}