package io.mishkav.generalparking.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import io.mishkav.generalparking.data.exceptions.PlaceNotReservatedException
import io.mishkav.generalparking.data.exceptions.PlaceReservationException
import io.mishkav.generalparking.domain.entities.*
import io.mishkav.generalparking.domain.repositories.IMapDatabaseRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import io.mishkav.generalparking.state.Session
import io.mishkav.generalparking.ui.utils.onValueListener
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class MapDatabaseRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: DatabaseReference
) : IMapDatabaseRepository {

    @Inject
    lateinit var session: Session

    private val _userState = MutableStateFlow(UserState.NOT_RESERVED)
    override val userState = _userState

    private val _alertState = MutableStateFlow(UserState.NOT_RESERVED)
    override val alertState = _alertState

    override fun changeUserState(state: UserState) {
        _userState.value = state
    }

    override fun changeAlertState(state: UserState) {
        _alertState.value = state
    }

    override suspend fun getParkingCoordinates(): Map<String, String> {
        return firebaseDatabase
            .child(PATH_TO_PARKING_COORDINATES)
            .get()
            .await()
            .getValue() as Map<String, String>
    }

    override suspend fun getReservationAddress(): String {
        return firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/reservation_address")
            .get()
            .await()
            .getValue() as String
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
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
        val timeReservation = LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of(TIME_ZONE)).format(formatter)

        //Check
        val checkReservation = firebaseDatabase
            .child("parking/$address/$floor/places/$placeCoordinates/value")
            .get()
            .await()
            .getValue() as Long

        if (checkReservation == 1L)
            throw PlaceReservationException()

        // Paths to info about Users, Parking, Users car
        val post = mapOf(
            "users_car/$address/$autoNumber" to firebaseAuth.currentUser?.uid,
            "users/${firebaseAuth.currentUser?.uid}/reservation_address" to address,
            "users/${firebaseAuth.currentUser?.uid}/reservation_place" to namePlace,
            "users/${firebaseAuth.currentUser?.uid}/reservation_level" to floor,
            "users/${firebaseAuth.currentUser?.uid}/time_reservation" to timeReservation,
            "parking/$address/$floor/places/$placeCoordinates/reservation" to firebaseAuth.currentUser?.uid,
            "parking/$address/$floor/places/$placeCoordinates/value" to 1
        )
        firebaseDatabase.updateChildren(post)
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

        // Paths to info about Users, Parking, Users car
        val post = mapOf(
            "users/${firebaseAuth.currentUser?.uid}/reservation_address" to EMPTY_STRING,
            "users/${firebaseAuth.currentUser?.uid}/reservation_place" to EMPTY_STRING,
            "users/${firebaseAuth.currentUser?.uid}/reservation_level" to EMPTY_STRING,
            "users/${firebaseAuth.currentUser?.uid}/time_reservation" to EMPTY_STRING,
            "parking/$address/$floor/places/$placeCoordinates/reservation" to SPACE,
            "parking/$address/$floor/places/$placeCoordinates/value" to 0,
            "users_car/$address/$autoNumber" to null
        )
        firebaseDatabase.updateChildren(post)
    }

    override suspend fun getAutoNumber(): String {
        return firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/number_auto")
            .get()
            .await()
            .getValue() as String
    }

    override suspend fun getTimeReservation(myCallback: TimeCallback) {

        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/time_reservation")
            .onValueListener(
                onDataChangeImpl = { dataSnapshot ->
                    val timeReservation = dataSnapshot.getValue() as String
                    Timber.tag(TAG).i("getTimeReservation: timeReservation = $timeReservation")

                    myCallback.onCallback(timeReservation)
                },
                onCancelledImpl = { error ->
                    Timber.tag(TAG).w(error.toException(), ON_CANCELLED_MESSAGE)
                }
            )
    }

    override suspend fun getBookingTime(): Long {
        return firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/remaining_booking_time")
            .get()
            .await()
            .getValue() as Long
    }

    override suspend fun getBookingRatio(
        address: String,
        floor: String
    ): Double {
        return firebaseDatabase
            .child("parking/$address/$floor/booking_ratio")
            .get()
            .await()
            .getValue() as Double
    }

    override suspend fun getTimeArrive(myCallback: TimeCallback) {

        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/time_arrive")
            .onValueListener(
                onDataChangeImpl = { dataSnapshot ->
                    val timeArrive = dataSnapshot.getValue() as String
                    Timber.tag(TAG).i("getTimeArrive: timeArrive = $timeArrive")

                    myCallback.onCallback(timeArrive)
                },
                onCancelledImpl = { error ->
                    Timber.tag(TAG).w(error.toException(), ON_CANCELLED_MESSAGE)
                }
            )
    }

    override suspend fun resetIsArrived() {
        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/arrive")
            .setValue(EMPTY_STRING)
            .await()
    }

    override suspend fun getAlertState() {

        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/arrive")
            .onValueListener(
                onDataChangeImpl = { dataSnapshot ->
                    val isArrived = dataSnapshot.getValue() as String
                    Timber.tag(TAG).i("getIsArrived: isArrived = $isArrived")
                    if (isArrived != EMPTY_STRING)
                        changeAlertState(UserState.ARRIVED)
                    else
                        changeAlertState(UserState.NOT_RESERVED)
                },
                onCancelledImpl = { error ->
                    Timber.tag(TAG).w(error.toException(), ON_CANCELLED_MESSAGE)
                }
            )

        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/exit")
            .onValueListener(
                onDataChangeImpl = { dataSnapshot ->
                    val isExit = dataSnapshot.getValue() as String
                    Timber.tag(TAG).i("getIsExit: isExit = $isExit")
                    if (isExit != EMPTY_STRING)
                        changeAlertState(UserState.EXIT)
                    else
                        changeAlertState(UserState.NOT_RESERVED)
                },
                onCancelledImpl = { error ->
                    Timber.tag(TAG).w(error.toException(), ON_CANCELLED_MESSAGE)
                }
            )
    }

    override suspend fun resetIsExit() {
        val post = mapOf(
            "exit" to EMPTY_STRING,
            "time_arrive" to EMPTY_STRING,
            "time_exit" to EMPTY_STRING,
            "time_reservation" to EMPTY_STRING
        )
        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}")
            .updateChildren(post)
    }

    override suspend fun getTimeExit(): String {
        return firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/time_exit")
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

        private const val ON_CANCELLED_MESSAGE = "loadPost:onCancelled"

        private const val EMPTY_STRING = ""
        private const val SPACE = " "
        private const val TAG = "MapDatabaseRepository"

        private val CLASS_STRING = String::class.java
        private val CLASS_INT = Int::class.java
        private val CLASS_FLOAT = Float::class.java
    }
}