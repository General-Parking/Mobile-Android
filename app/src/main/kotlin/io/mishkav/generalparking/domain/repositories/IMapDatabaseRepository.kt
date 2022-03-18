package io.mishkav.generalparking.domain.repositories

import io.mishkav.generalparking.domain.entities.ParkingScheme

interface IMapDatabaseRepository {
    suspend fun getParkingCoordinates(): Map<String, String>
    suspend fun getParkingScheme(address: String, floor: Int): ParkingScheme
    suspend fun setParkingPlaceReservation(
        address: String,
        namePlace: String,
        floor: Int,
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
}