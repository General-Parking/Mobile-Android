package io.mishkav.generalparking.domain.repositories

import io.mishkav.generalparking.domain.entities.User

interface IAuthDatabaseRepository {
    suspend fun insertUserData(user: User)
    suspend fun getUserDataFromDatabase(): User
    suspend fun isMinSdkVersionApproved(): Boolean
}