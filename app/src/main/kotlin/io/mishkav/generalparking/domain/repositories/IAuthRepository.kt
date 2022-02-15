package io.mishkav.generalparking.domain.repositories

interface IAuthRepository {

    fun isUserAuthenticated(): Boolean
    fun signOut()
}