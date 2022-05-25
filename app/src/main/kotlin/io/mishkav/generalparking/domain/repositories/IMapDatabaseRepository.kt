package io.mishkav.generalparking.domain.repositories

import io.mishkav.generalparking.domain.entities.ParkingScheme
import io.mishkav.generalparking.domain.entities.ParkingShortInfo
import io.mishkav.generalparking.domain.entities.TimeCallback

interface IMapDatabaseRepository {
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
    suspend fun getTimeReservation(myCallback: TimeCallback)
    suspend fun getBookingTime(): Long
    suspend fun getBookingRatio(
        address: String,
        floor: String
    ): Double
    suspend fun getTimeArrive(myCallback: TimeCallback)
    suspend fun getIsArrived()
    suspend fun resetIsArrived()
    suspend fun getIsExit()
    suspend fun resetIsExit()
    suspend fun getTimeExit(): String
    suspend fun getParkingShortInfo(): Map<String, ParkingShortInfo>
}