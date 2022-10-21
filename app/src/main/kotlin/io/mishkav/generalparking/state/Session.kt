package io.mishkav.generalparking.state

import android.content.Context
import androidx.core.content.edit
import io.mishkav.generalparking.ui.screens.payment.config.PaymentConfig
import io.mishkav.generalparking.ui.screens.payment.config.PaymentMethods
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

    private val _selectedOption = MutableStateFlow(sharedPreferences.getString(PREF_SELECTED_OPTION, PaymentConfig.paymentMethods[1].title).orEmpty())
    val selectedOption: StateFlow<String> = _selectedOption

    private val _getSavedParams = MutableStateFlow(sharedPreferences.getString(PREF_GET_SAVED_PARAMS, "").orEmpty())
    val getSavedParams: StateFlow<String> = _getSavedParams

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

    fun changeSelectedOption(method: PaymentMethods) {
        sharedPreferences.edit { putString(PREF_SELECTED_PARKING_FLOOR, method.title) }
        _selectedOption.value = method.title
    }

    fun changeGetSavedParams(flag: String) {
        sharedPreferences.edit { putString(PREF_GET_SAVED_PARAMS, flag) }
        _getSavedParams.value = flag
    }

    companion object {
        const val PREF_CURRENT_PARKING_ADDRESS = "currentParkingAddress"
        const val PREF_AUTO_NUMBER = "autoNumber"
        const val PREF_SELECTED_PARKING_PLACE = "selectedParkingPlaceName"
        const val PREF_SELECTED_PARKING_COORDINATES = "selectedParkingPlaceCoordinates"
        const val PREF_SELECTED_PARKING_FLOOR = "_selectedParkingPlaceFloor"
        const val PREF_SELECTED_OPTION = "selectedOption"
        const val PREF_GET_SAVED_PARAMS = "getSavedParams"

        const val AGREEMENT_URI = "https://www.genparking.com/пользовательское-соглашение"
        const val PREF_NAME = "session"
    }
}