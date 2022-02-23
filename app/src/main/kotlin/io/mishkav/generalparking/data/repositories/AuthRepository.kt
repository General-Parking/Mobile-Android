package io.mishkav.generalparking.data.repositories

import com.google.firebase.auth.FirebaseAuth
import io.mishkav.generalparking.data.exceptions.EmailNotVerifiedException
import io.mishkav.generalparking.data.exceptions.NullUserException
import io.mishkav.generalparking.domain.repositories.IAuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : IAuthRepository {

    override fun isUserAuthenticated(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun isEmailVerified(): Boolean? {
        return firebaseAuth.currentUser?.isEmailVerified
    }

    override fun signOut() {
        firebaseAuth.currentUser?.delete()
    }

    override suspend fun signInWithEmailAndPassword(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()

        isEmailVerified().let { verified ->
            if (verified == null)
                throw NullUserException()
            if (!verified)
                throw EmailNotVerifiedException()
        }
    }

    override suspend fun createUserWithEmailAndPassword(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        sendEmailVerification(email)
    }

    override suspend fun sendEmailVerification(email: String) {
        firebaseAuth.currentUser.let { user ->
            if (user == null)
                throw NullUserException()
            user.sendEmailVerification()
        }
    }

    override suspend fun sendPasswordResetEmail(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).await()
    }
}