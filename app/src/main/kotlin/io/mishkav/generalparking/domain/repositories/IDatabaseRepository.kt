package io.mishkav.generalparking.domain.repositories

import io.mishkav.generalparking.domain.entities.User

interface IDatabaseRepository {
    suspend fun insertUserData(user: User)
}