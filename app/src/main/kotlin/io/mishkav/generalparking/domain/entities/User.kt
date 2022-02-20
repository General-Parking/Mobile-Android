package io.mishkav.generalparking.domain.entities

import com.google.gson.annotations.SerializedName

data class User(
    val email: String,
    val name: String,
    @SerializedName("number_auto")
    val numberAuto: String,
    @SerializedName("profileimage")
    val profileImage: String,
    val massiv: List<String>,
    @SerializedName("reservation_adress")
    val reservationAddress: String,
    @SerializedName("reservation_level")
    val reservationLevel: String,
    @SerializedName("reservation_place")
    val reservationPlace: String,
    val uid: String,
    @SerializedName("time_reservation")
    val timeReservation: Long,
    @SerializedName("time_arrive")
    val timeArrive: Long,
    @SerializedName("time_exit")
    val timeExit: Long,
    val arrive: Int,
    val exit: Int
)