package io.mishkav.generalparking.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import io.mishkav.generalparking.data.utils.toMap
import io.mishkav.generalparking.domain.entities.User
import io.mishkav.generalparking.domain.repositories.IDatabaseRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: DatabaseReference
) : IDatabaseRepository {

    private val currentUserUid: String
        get() = firebaseAuth.currentUser!!.uid

    override suspend fun insertUserData(user: User) {
        firebaseDatabase
            .child(PATH_TO_USERS)
            .child(currentUserUid)
            .setValue(user.toMap())
            .await()
    }

    companion object {
        private const val PATH_TO_USERS = "users"
    }
}