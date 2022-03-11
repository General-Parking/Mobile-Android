package io.mishkav.generalparking.state

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class Session(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val _currentParkingAddress = MutableStateFlow(sharedPreferences.getString(CURRENT_PARKING_ADDRESS, "").orEmpty())
    val currentParkingAddress: StateFlow<String> = _currentParkingAddress

    fun changeCurrentParkingAddress(address: String) {
        sharedPreferences.edit { putString(CURRENT_PARKING_ADDRESS, address) }
        _currentParkingAddress.value = address
    }

    companion object {
        const val CURRENT_PARKING_ADDRESS = "currentParkingAddress"

        const val AGREEMENT_URI = "https://www.genparking.com/пользовательское-соглашение"
        const val PREF_NAME = "session"
    }
}