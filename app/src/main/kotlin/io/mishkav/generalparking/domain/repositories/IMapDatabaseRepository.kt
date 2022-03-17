package io.mishkav.generalparking.domain.repositories

import io.mishkav.generalparking.domain.entities.ParkingScheme

interface IMapDatabaseRepository {
    suspend fun getParkingCoordinates(): Map<String, String>
    suspend fun getParkingScheme(address: String, floor: Int): ParkingScheme
}