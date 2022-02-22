package io.mishkav.generalparking.data.utils

import io.mishkav.generalparking.data.utils.UserFields.DefaultFields.DEFAULT_STRING_FIELD
import io.mishkav.generalparking.domain.entities.User

fun User.toMap(): Map<String, *> {
    with(UserFields) {
        return mapOf(
            FIELD_EMAIL to arrive,
            FIELD_NAME to name,
            FIELD_NUMBER_AUTO to numberAuto,
            FIELD_PROFILE_IMAGE to profileImage,
            FIELD_META_USER_INFO to metaUserInfo,
            FIELD_RESERVATION_ADDRESS to reservationAddress,
            FIELD_RESERVATION_LEVEL to reservationLevel,
            FIELD_RESERVATION_PLACE to reservationPlace,
            FIELD_TIME_RESERVATION to timeReservation,
            FIELD_TIME_ARRIVE to timeArrive,
            FIELD_TIME_EXIT to timeExit,
            FIELD_ARRIVE to arrive,
            FIELD_EXIT to exit
        )
    }
}

fun getMetaUserInfoInstance(
    carBrand: String = DEFAULT_STRING_FIELD,
    name: String = DEFAULT_STRING_FIELD,
    phoneNumber: String = DEFAULT_STRING_FIELD
): Map<String, String> {
    with(UserFields) {
        return mapOf(
            FIELD_CAR_BRAND to carBrand,
            FIELD_NAME to name,
            FIELD_PHONE_NUMBER to phoneNumber
        )
    }
}

object UserFields {
    const val FIELD_EMAIL = "email"
    const val FIELD_NAME = "name"
    const val FIELD_NUMBER_AUTO = "number_auto"
    const val FIELD_PROFILE_IMAGE = "profile_image"
    const val FIELD_META_USER_INFO = "meta_user_info"
    const val FIELD_RESERVATION_ADDRESS = "reservation_address"
    const val FIELD_RESERVATION_LEVEL = "reservation_level"
    const val FIELD_RESERVATION_PLACE = "reservation_place"
    const val FIELD_TIME_RESERVATION = "time_reservation"
    const val FIELD_TIME_ARRIVE = "time_arrive"
    const val FIELD_TIME_EXIT = "time_exit"
    const val FIELD_ARRIVE = "arrive"
    const val FIELD_EXIT = "exit"
    const val FIELD_CAR_BRAND = "car_brand"
    const val FIELD_PHONE_NUMBER = "phone_number"

    object DefaultFields {
        const val DEFAULT_STRING_FIELD = ""
        const val DEFAULT_INT_FIELD = 0
        const val DEFAULT_REMAINING_BOOKING_TIME = 60
        const val DEFAULT_IMAGE_PATH =
            "https://firebasestorage.googleapis.com/v0/b/gdeparkovka-6360b.appspot.com/o/space.jpeg?alt=media&token=9491c0bc-b8d9-44fb-be0d-58f1191a8a1e"
    }
}