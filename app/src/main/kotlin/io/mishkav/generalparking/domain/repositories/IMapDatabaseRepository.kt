package io.mishkav.generalparking.domain.repositories

import io.mishkav.generalparking.data.repositories.MapDatabaseRepository
import io.mishkav.generalparking.domain.entities.ParkingScheme

interface IMapDatabaseRepository {
    suspend fun getParkingCoordinates(): Map<String, String>
    suspend fun getParkingScheme(address: String, floor: Int): ParkingScheme
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
    suspend fun getTimeReservation(myCallback: MapDatabaseRepository.TimeCallback): String
    suspend fun getBookingTime(): Long
    suspend fun getTimeArrive(myCallback: MapDatabaseRepository.TimeCallback): String
    suspend fun getIsArrived(): String
    suspend fun resetIsArrived()
    suspend fun getIsExit(): String
    suspend fun resetIsExit()
    suspend fun getTimeExit(): String
}