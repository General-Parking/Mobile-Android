package io.mishkav.generalparking.data.repositories

import com.google.firebase.auth.FirebaseAuth
import io.mishkav.generalparking.domain.repositories.IAuthRepository
import timber.log.Timber
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

    override suspend fun signInWithEmailAndPassword(email: String, password: String) {
        firebaseAuth.currentUser?.delete()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseAuth.currentUser!!.let { user ->
                        if (!user.isEmailVerified) {

                        }
                    }
                } else
                    Timber.wtf("Bad authorization")
            }
    }
}