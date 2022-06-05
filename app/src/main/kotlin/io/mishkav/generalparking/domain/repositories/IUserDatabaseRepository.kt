package io.mishkav.generalparking.domain.repositories

import io.mishkav.generalparking.domain.entities.User

interface IUserDatabaseRepository {
    suspend fun insertUserData(user: User)
    suspend fun getUserDataFromDatabase(): User
    suspend fun getUserBalance(): Int
    suspend fun isMinSdkVersionApproved(): Boolean
}