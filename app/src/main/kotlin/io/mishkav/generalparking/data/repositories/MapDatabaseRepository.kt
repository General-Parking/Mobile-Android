package io.mishkav.generalparking.data.repositories

import android.content.ContentValues.TAG
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import io.mishkav.generalparking.data.exceptions.PlaceNotReservatedException
import io.mishkav.generalparking.data.exceptions.PlaceReservationException
import io.mishkav.generalparking.domain.entities.ParkingPlace
import io.mishkav.generalparking.domain.entities.ParkingScheme
import io.mishkav.generalparking.domain.repositories.IMapDatabaseRepository
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import javax.inject.Inject
import androidx.compose.runtime.setValue
import io.mishkav.generalparking.state.Session
import timber.log.Timber
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class MapDatabaseRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: DatabaseReference
) : IMapDatabaseRepository {

    @Inject
    lateinit var session: Session


    class TimeMutable {
        var time by mutableStateOf("")
    }

    interface TimeCallback {
        fun onCallback(value: String)
    }

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
            val name = place.child(PATH_TO_PLACE_NAME).getValue(CLASS_STRING)!!
            val rotation = place.child(PATH_TO_PLACE_ROTATE).getValue(CLASS_INT)!!
            val value = place.child(PATH_TO_PLACE_VALUE).getValue(CLASS_INT)!!

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

    override suspend fun setParkingPlaceReservation(
        address: String,
        namePlace: String,
        floor: String,
        placeCoordinates: String, //i_j
        autoNumber: String
    ) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
        val timeReservation = OffsetDateTime.now(ZoneOffset.UTC).format(formatter)

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
            .child("users_car/$address")
            .setValue(hashMapOf(autoNumber to firebaseAuth.currentUser?.uid))
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

    override suspend fun getTimeReservation(myCallback:TimeCallback) {
        val timeReservation = TimeMutable()

        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/time_reservation")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    timeReservation.time = dataSnapshot.getValue() as String
                    Timber.tag(TAG).i(timeReservation.time)

                    myCallback.onCallback(timeReservation.time)
                    if (timeReservation.time == EMPTY_STRING)
                        session.changeUserState(EMPTY_STRING)
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.tag(TAG).w(error.toException(), "loadPost:onCancelled")
                }
            })
    }

    override suspend fun getBookingTime(): Long {
        return firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/remaining_booking_time")
            .get()
            .await()
            .getValue() as Long
    }

    override suspend fun getPriceParking(
        address: String,
        floor: String
    ): Long {
        return firebaseDatabase
            .child("parking/$address/$floor/price_parking")
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

    override suspend fun getTimeArrive(myCallback:TimeCallback) {
        val timeArrive = TimeMutable()

        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/time_arrive")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    timeArrive.time = dataSnapshot.getValue() as String
                    Timber.tag(TAG).i(timeArrive.time)

                    myCallback.onCallback(timeArrive.time)
//                    if (timeArrive.time != EMPTY_STRING)
//                        session.changeUserState("arrived")
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.tag(TAG).w(error.toException(), "loadPost:onCancelled")
                }
            })
    }

    override suspend fun getIsArrived(): String {
        val isArrived = TimeMutable()

        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/arrive")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    isArrived.time = dataSnapshot.getValue() as String
                    Timber.tag(TAG).i(isArrived.time)
                    if (isArrived.time != "")
                        session.changeIsArrived("arrived")
                    else
                        session.changeIsArrived(EMPTY_STRING)
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.tag(TAG).w(error.toException(), "loadPost:onCancelled")
                }
            })
        return isArrived.time
    }

    override suspend fun resetIsArrived() {
        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/arrive")
            .setValue(EMPTY_STRING)
            .await()
    }

    override suspend fun getIsExit(): String {
        val isExit = TimeMutable()

        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/exit")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    isExit.time = dataSnapshot.getValue() as String
                    Timber.tag(TAG).i(isExit.time)
                    if (isExit.time != "")
                        session.changeIsExit("exit")
                    else
                        session.changeIsExit(EMPTY_STRING)
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.tag(TAG).w(error.toException(), "loadPost:onCancelled")
                }
            })
        return isExit.time
    }

    override suspend fun resetIsExit() {
        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/exit")
            .setValue(EMPTY_STRING)
            .await()
        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/time_arrive")
            .setValue(EMPTY_STRING)
            .await()
        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/time_exit")
            .setValue(EMPTY_STRING)
            .await()
        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/time_reservation")
            .setValue(EMPTY_STRING)
            .await()
        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/reservation_address")
            .setValue(EMPTY_STRING)
            .await()
        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/reservation_level")
            .setValue(EMPTY_STRING)
            .await()
        firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/reservation_place")
            .setValue(EMPTY_STRING)
            .await()
    }

    override suspend fun getTimeExit(): String {
        return firebaseDatabase
            .child("users/${firebaseAuth.currentUser?.uid}/time_exit")
            .get()
            .await()
            .getValue() as String
    }

    companion object {
        private const val PATH_TO_PARKING_COORDINATES = "parking/coordinates_address"
        private const val PATH_TO_WIDTH = "m"
        private const val PATH_TO_HEIGHT = "n"
        private const val PATH_TO_PLACES = "places"
        private const val PATH_TO_PLACE_NAME = "name"
        private const val PATH_TO_PLACE_ROTATE = "rotate"
        private const val PATH_TO_PLACE_VALUE = "value"

        private const val EMPTY_STRING = ""
        private const val SPACE = " "

        private val CLASS_STRING = String::class.java
        private val CLASS_INT = Int::class.java
    }
}