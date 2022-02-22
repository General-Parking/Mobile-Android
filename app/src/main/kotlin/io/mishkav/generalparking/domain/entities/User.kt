package io.mishkav.generalparking.domain.entities

import com.google.gson.annotations.SerializedName
import io.mishkav.generalparking.data.utils.UserFields.DefaultFields.DEFAULT_IMAGE_PATH
import io.mishkav.generalparking.data.utils.UserFields.DefaultFields.DEFAULT_INT_FIELD
import io.mishkav.generalparking.data.utils.UserFields.DefaultFields.DEFAULT_REMAINING_BOOKING_TIME
import io.mishkav.generalparking.data.utils.UserFields.DefaultFields.DEFAULT_STRING_FIELD
import io.mishkav.generalparking.data.utils.getMetaUserInfoInstance

data class User(
    @SerializedName("account_balance")
    val accountBalance: Int,
    val arrive: String,
    val email: String,
    val exit: String,
    @SerializedName("meta_user_info")
    val metaUserInfo: Map<String, String>,
    val name: String,
    @SerializedName("number_auto")
    val numberAuto: String,
    @SerializedName("profile_image")
    val profileImage: String,
    @SerializedName("remaining_booking_time")
    val remainingBookingTime: Int,
    @SerializedName("reservation_address")
    val reservationAddress: String,
    @SerializedName("reservation_level")
    val reservationLevel: String,
    @SerializedName("reservation_place")
    val reservationPlace: String,
    @SerializedName("time_arrive")
    val timeArrive: String,
    @SerializedName("time_reservation")
    val timeReservation: String,
    @SerializedName("time_exit")
    val timeExit: String
) {
    companion object {
        fun getInstance(
            accountBalance: Int = DEFAULT_INT_FIELD,
            arrive: String = DEFAULT_STRING_FIELD,
            carBrand: String = DEFAULT_STRING_FIELD,
            email: String = DEFAULT_STRING_FIELD,
            exit: String = DEFAULT_STRING_FIELD,
            name: String = DEFAULT_STRING_FIELD,
            numberAuto: String = DEFAULT_STRING_FIELD,
            phoneNumber: String = DEFAULT_STRING_FIELD,
            profileImage: String = DEFAULT_IMAGE_PATH,
            remainingBookingTime: Int = DEFAULT_REMAINING_BOOKING_TIME,
            reservationAddress: String = DEFAULT_STRING_FIELD,
            reservationLevel: String = DEFAULT_STRING_FIELD,
            reservationPlace: String = DEFAULT_STRING_FIELD,
            timeArrive: String = DEFAULT_STRING_FIELD,
            timeReservation: String = DEFAULT_STRING_FIELD,
            timeExit: String = DEFAULT_STRING_FIELD
        ): User {
            return User(
                accountBalance = accountBalance,
                arrive = arrive,
                email = email,
                exit = exit,
                metaUserInfo = getMetaUserInfoInstance(
                    carBrand = carBrand,
                    name = name,
                    phoneNumber = phoneNumber
                ),
                name = name,
                numberAuto = numberAuto,
                profileImage = profileImage,
                remainingBookingTime = remainingBookingTime,
                reservationAddress = reservationAddress,
                reservationLevel = reservationLevel,
                reservationPlace = reservationPlace,
                timeArrive = timeArrive,
                timeReservation = timeReservation,
                timeExit = timeExit,
            )
        }
    }
}