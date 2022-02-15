package io.mishkav.generalparking.data.repositories

import com.google.firebase.auth.FirebaseAuth
import io.mishkav.generalparking.domain.repositories.IAuthRepository
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : IAuthRepository {

    override fun isUserAuthenticated(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun signOut() {
        firebaseAuth.currentUser?.delete()
    }
}