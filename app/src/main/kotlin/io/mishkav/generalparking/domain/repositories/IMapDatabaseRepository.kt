package io.mishkav.generalparking.domain.repositories

interface IMapDatabaseRepository {
    suspend fun getParkingCoordinates(): Map<String, String>
}