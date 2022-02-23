package io.mishkav.generalparking.domain.repositories

interface IAuthRepository {
    fun isUserAuthenticated(): Boolean
    fun isEmailVerified(): Boolean?
    fun signOut()

    suspend fun signInWithEmailAndPassword(email: String, password: String)
    suspend fun createUserWithEmailAndPassword(email: String, password: String)
    suspend fun sendEmailVerification(email: String)
    suspend fun sendPasswordResetEmail(email: String)
}
