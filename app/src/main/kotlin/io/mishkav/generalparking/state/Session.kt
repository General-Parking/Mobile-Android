package io.mishkav.generalparking.state

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class Session(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val _currentParkingAddress = MutableStateFlow(sharedPreferences.getString(PREF_CURRENT_PARKING_ADDRESS, "").orEmpty())
    val currentParkingAddress: StateFlow<String> = _currentParkingAddress

    private val _autoNumber = MutableStateFlow(sharedPreferences.getString(PREF_AUTO_NUMBER, "").orEmpty())
    val autoNumber: StateFlow<String> = _autoNumber

    private val _selectedParkingPlace = MutableStateFlow(sharedPreferences.getString(PREF_SELECTED_PARKING_PLACE, "").orEmpty())
    val selectedParkingPlace: StateFlow<String> = _selectedParkingPlace

    private val _selectedParkingPlaceCoordinates = MutableStateFlow(sharedPreferences.getString(PREF_SELECTED_PARKING_COORDINATES, "").orEmpty())
    val selectedParkingPlaceCoordinates: StateFlow<String> = _selectedParkingPlaceCoordinates

    private val _isCurrentUserReservedParkingPlace = MutableStateFlow(sharedPreferences.getBoolean(PREF_IS_CURRENT_USER_RESERVED_PLACE, false))
    val isCurrentUserReservedParkingPlace: StateFlow<Boolean> = _isCurrentUserReservedParkingPlace

    fun changeCurrentParkingAddress(address: String) {
        sharedPreferences.edit { putString(PREF_CURRENT_PARKING_ADDRESS, address) }
        _currentParkingAddress.value = address
    }

    fun changeAutoNumber(autoNumber: String) {
        sharedPreferences.edit { putString(PREF_AUTO_NUMBER, autoNumber) }
        _autoNumber.value = autoNumber
    }

    // Set reserved name
    fun changeSelectedParkingPlace(namePlace: String) {
        sharedPreferences.edit { putString(PREF_SELECTED_PARKING_PLACE, namePlace) }
        _selectedParkingPlace.value = namePlace
    }

    fun changeSelectedParkingPlaceCoordinates(coordinates: String) {
        sharedPreferences.edit { putString(PREF_SELECTED_PARKING_COORDINATES, coordinates) }
        _selectedParkingPlaceCoordinates.value = coordinates
    }

    fun changeIsCurrentUserReservedParkingPlace(isReserved: Boolean) {
        sharedPreferences.edit { putBoolean(PREF_IS_CURRENT_USER_RESERVED_PLACE, isReserved) }
        _isCurrentUserReservedParkingPlace.value = isReserved
    }

    companion object {
        const val PREF_CURRENT_PARKING_ADDRESS = "currentParkingAddress"
        const val PREF_AUTO_NUMBER = "autoNumber"
        const val PREF_SELECTED_PARKING_PLACE = "selectedParkingPlace"
        const val PREF_SELECTED_PARKING_COORDINATES = "selectedParkingPlaceCoordinates"
        const val PREF_IS_CURRENT_USER_RESERVED_PLACE = "isCurrentUserReservedParkingPlace"

        const val AGREEMENT_URI = "https://www.genparking.com/пользовательское-соглашение"
        const val PREF_NAME = "session"
    }
}