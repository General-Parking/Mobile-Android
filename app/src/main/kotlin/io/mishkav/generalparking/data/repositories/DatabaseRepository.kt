package io.mishkav.generalparking.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import io.mishkav.generalparking.data.exceptions.NullUserException
import io.mishkav.generalparking.data.utils.UserFields
import io.mishkav.generalparking.data.utils.UserFields.DefaultFields.DEFAULT_STRING_FIELD
import io.mishkav.generalparking.data.utils.UserFields.DefaultFields.DEFAULT_INT_FIELD
import io.mishkav.generalparking.data.utils.UserFields.DefaultFields.DEFAULT_IMAGE_PATH
import io.mishkav.generalparking.data.utils.UserFields.DefaultFields.DEFAULT_REMAINING_BOOKING_TIME
import io.mishkav.generalparking.data.utils.toMap
import io.mishkav.generalparking.domain.entities.User
import io.mishkav.generalparking.domain.repositories.IDatabaseRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: DatabaseReference
) : IDatabaseRepository {

    private val currentUserUid: String?
        get() = firebaseAuth.currentUser?.uid

    override suspend fun insertUserData(user: User) {
        currentUserUid.let { uid ->
            if (uid == null)
                throw NullUserException()

            firebaseDatabase
                .child(PATH_TO_USERS)
                .child(uid)
                .setValue(user.toMap())
                .await()
        }
    }

    override suspend fun getUserDataFromDatabase(): User {
        currentUserUid.let { uid ->
            if (uid == null)
                throw NullUserException()

            val rawUser = firebaseDatabase
                .child(PATH_TO_USERS)
                .child(uid)
                .get()
                .await()

            with(UserFields) {
                return User(
                    accountBalance = rawUser.child(FIELD_ACCOUNT_BALANCE).getValue(CLASS_INT) ?: DEFAULT_INT_FIELD,
                    arrive = rawUser.child(FIELD_ARRIVE).getValue(CLASS_STRING) ?: DEFAULT_STRING_FIELD,
                    email = rawUser.child(FIELD_EMAIL).getValue(CLASS_STRING) ?: DEFAULT_STRING_FIELD,
                    exit = rawUser.child(FIELD_EXIT).getValue(CLASS_STRING) ?: DEFAULT_STRING_FIELD,
                    metaUserInfo = mapOf(
                        FIELD_CAR_BRAND to (
                            rawUser.child(FIELD_META_USER_INFO).child(FIELD_CAR_BRAND).getValue(CLASS_STRING)
                                ?: DEFAULT_STRING_FIELD
                            ),
                        FIELD_NAME to (
                            rawUser.child(FIELD_META_USER_INFO).child(FIELD_NAME).getValue(CLASS_STRING)
                                ?: DEFAULT_STRING_FIELD
                            ),
                        FIELD_PHONE_NUMBER to (
                            rawUser.child(FIELD_META_USER_INFO).child(FIELD_PHONE_NUMBER).getValue(CLASS_STRING)
                                ?: DEFAULT_STRING_FIELD
                            )
                    ),
                    name = rawUser.child(FIELD_NAME).getValue(CLASS_STRING) ?: DEFAULT_STRING_FIELD,
                    numberAuto = rawUser.child(FIELD_NUMBER_AUTO).getValue(CLASS_STRING) ?: DEFAULT_STRING_FIELD,
                    profileImage = rawUser.child(FIELD_PROFILE_IMAGE).getValue(CLASS_STRING) ?: DEFAULT_IMAGE_PATH,
                    remainingBookingTime = rawUser.child(FIELD_REMAINING_BOOKING_TIME).getValue(CLASS_INT)
                        ?: DEFAULT_REMAINING_BOOKING_TIME,
                    reservationAddress = rawUser.child(FIELD_RESERVATION_ADDRESS).getValue(CLASS_STRING)
                        ?: DEFAULT_STRING_FIELD,
                    reservationLevel = rawUser.child(FIELD_RESERVATION_LEVEL).getValue(CLASS_STRING)
                        ?: DEFAULT_STRING_FIELD,
                    reservationPlace = rawUser.child(FIELD_RESERVATION_PLACE).getValue(CLASS_STRING)
                        ?: DEFAULT_STRING_FIELD,
                    timeArrive = rawUser.child(FIELD_TIME_ARRIVE).getValue(CLASS_STRING) ?: DEFAULT_STRING_FIELD,
                    timeReservation = rawUser.child(FIELD_TIME_RESERVATION).getValue(CLASS_STRING)
                        ?: DEFAULT_STRING_FIELD,
                    timeExit = rawUser.child(FIELD_TIME_EXIT).getValue(CLASS_STRING) ?: DEFAULT_STRING_FIELD
                )
            }
        }
    }

    companion object {
        private const val PATH_TO_USERS = "users"

        private val CLASS_STRING = String::class.java
        private val CLASS_INT = Int::class.java
    }
}