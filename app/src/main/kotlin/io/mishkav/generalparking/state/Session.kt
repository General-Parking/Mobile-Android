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

    private val _selectedParkingPlaceName = MutableStateFlow(sharedPreferences.getString(PREF_SELECTED_PARKING_PLACE, "").orEmpty())
    val selectedParkingPlaceName: StateFlow<String> = _selectedParkingPlaceName

    private val _selectedParkingPlaceCoordinates = MutableStateFlow(sharedPreferences.getString(PREF_SELECTED_PARKING_COORDINATES, "").orEmpty())
    val selectedParkingPlaceCoordinates: StateFlow<String> = _selectedParkingPlaceCoordinates

    private val _selectedParkingPlaceFloor = MutableStateFlow(sharedPreferences.getString(PREF_SELECTED_PARKING_FLOOR, "").orEmpty())
    val selectedParkingPlaceFloor: StateFlow<String> = _selectedParkingPlaceFloor

    private val _userState = MutableStateFlow(sharedPreferences.getString(PREF_USER_STATE, "").orEmpty())
    val userState: StateFlow<String> = _userState

    private val _isArrived = MutableStateFlow(sharedPreferences.getString(PREF_IS_ARRIVED, "").orEmpty())
    val isArrived: StateFlow<String> = _isArrived

    private val _isExit = MutableStateFlow(sharedPreferences.getString(PREF_IS_EXIT, "").orEmpty())
    val isExit: StateFlow<String> = _isExit

    fun changeCurrentParkingAddress(address: String) {
        sharedPreferences.edit { putString(PREF_CURRENT_PARKING_ADDRESS, address) }
        _currentParkingAddress.value = address
    }

    fun changeAutoNumber(autoNumber: String) {
        sharedPreferences.edit { putString(PREF_AUTO_NUMBER, autoNumber) }
        _autoNumber.value = autoNumber
    }

    // Set reserved name
    fun changeSelectedParkingPlaceName(namePlace: String) {
        sharedPreferences.edit { putString(PREF_SELECTED_PARKING_PLACE, namePlace) }
        _selectedParkingPlaceName.value = namePlace
    }

    fun changeSelectedParkingPlaceCoordinates(coordinates: String) {
        sharedPreferences.edit { putString(PREF_SELECTED_PARKING_COORDINATES, coordinates) }
        _selectedParkingPlaceCoordinates.value = coordinates
    }

    fun changeSelectedParkingPlaceFloor(floor: String) {
        sharedPreferences.edit { putString(PREF_SELECTED_PARKING_FLOOR, floor) }
        _selectedParkingPlaceFloor.value = floor
    }


    fun changeUserState(state: String) {
        sharedPreferences.edit { putString(PREF_USER_STATE, state) }
        _userState.value = state
    }

    fun changeIsArrived(isArrived: String) {
        sharedPreferences.edit { putString(PREF_IS_ARRIVED, isArrived) }
        _isArrived.value = isArrived
    }

    fun changeIsExit(isExit: String) {
        sharedPreferences.edit { putString(PREF_IS_EXIT, isExit) }
        _isExit.value = isExit
    }

    companion object {
        const val PREF_CURRENT_PARKING_ADDRESS = "currentParkingAddress"
        const val PREF_AUTO_NUMBER = "autoNumber"
        const val PREF_SELECTED_PARKING_PLACE = "selectedParkingPlaceName"
        const val PREF_SELECTED_PARKING_COORDINATES = "selectedParkingPlaceCoordinates"
        const val PREF_SELECTED_PARKING_FLOOR = "_selectedParkingPlaceFloor"
        const val PREF_USER_STATE = "userState"
        const val PREF_IS_ARRIVED = "isArrived"
        const val PREF_IS_EXIT = "isExit"

        const val AGREEMENT_URI = "https://www.genparking.com/пользовательское-соглашение"
        const val PREF_NAME = "session"
    }
}