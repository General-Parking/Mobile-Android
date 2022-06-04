package io.mishkav.generalparking.domain.repositories

import io.mishkav.generalparking.domain.entities.ParkingScheme
import io.mishkav.generalparking.domain.entities.ParkingShortInfo
import io.mishkav.generalparking.domain.entities.TimeCallback
import io.mishkav.generalparking.domain.entities.UserState
import kotlinx.coroutines.flow.StateFlow

interface IMapDatabaseRepository {
    // userState - состояние пользователя: NOT_RESERVED, RESERVED, ARRIVED
    // alertState - состояние уведомления: NOT_RESERVED, EXIT, ARRIVED
    val userState: StateFlow<UserState>
    val alertState: StateFlow<UserState>

    // Change states
    fun changeUserState(state: UserState)
    fun changeAlertState(state: UserState)

    // Get fields about parking
    suspend fun getParkingCoordinates(): Map<String, String>
    suspend fun getParkingScheme(address: String): Map<String, ParkingScheme>
    suspend fun getReservationAddress(): String
    suspend fun setParkingPlaceReservation(
        address: String,
        namePlace: String,
        floor: String,
        placeCoordinates: String,
        autoNumber: String
    )
    suspend fun removeParkingPlaceReservation(
        address: String,
        autoNumber: String,
        floor: Int,
        placeCoordinates: String
    )
    suspend fun getAutoNumber(): String

    // Get fields for states
    suspend fun getTimeReservation(myCallback: TimeCallback)
    suspend fun getBookingTime(): Long
    suspend fun getBookingRatio(
        address: String,
        floor: String
    ): Double
    suspend fun getTimeArrive(myCallback: TimeCallback)
    suspend fun getAlertState()
    suspend fun getTimeExit(): String
    suspend fun getParkingShortInfo(): Map<String, ParkingShortInfo>

    // Reset fields for alertState
    suspend fun resetIsArrived()
    suspend fun resetIsExit()
}