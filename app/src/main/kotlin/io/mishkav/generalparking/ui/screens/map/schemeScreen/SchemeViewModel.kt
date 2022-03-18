package io.mishkav.generalparking.ui.screens.map.schemeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.mishkav.generalparking.GeneralParkingApp
import io.mishkav.generalparking.R
import io.mishkav.generalparking.dagger.AppComponent
import io.mishkav.generalparking.domain.entities.ParkingScheme
import io.mishkav.generalparking.domain.entities.User
import io.mishkav.generalparking.domain.repositories.IAuthDatabaseRepository
import io.mishkav.generalparking.domain.repositories.IMapDatabaseRepository
import io.mishkav.generalparking.state.Session
import io.mishkav.generalparking.ui.screens.map.schemeScreen.components.ParkingPlaceState
import io.mishkav.generalparking.ui.screens.map.schemeScreen.components.ParkingSchemeConsts
import io.mishkav.generalparking.ui.utils.MutableResultFlow
import io.mishkav.generalparking.ui.utils.loadOrError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SchemeViewModel(appComponent: AppComponent = GeneralParkingApp.appComponent) : ViewModel() {
    @Inject
    lateinit var mapDatabaseRepository: IMapDatabaseRepository

    @Inject
    lateinit var session: Session

    @Inject
    lateinit var authDatabaseRepository: IAuthDatabaseRepository

    val parkingSchemeResult = MutableResultFlow<ParkingScheme>()
    val setParkingPlaceReservationResult = MutableResultFlow<Unit>()
    val removeParkingPlaceReservationResult = MutableResultFlow<Unit>()

    val currentParkingAddress by lazy { session.currentParkingAddress }
    private val autoNumber by lazy { session.autoNumber }

    private val _selectedParkingPlace by lazy { session.selectedParkingPlace }
    val selectedParkingPlace = MutableStateFlow<String>(ParkingSchemeConsts.EMPTY_STRING)

    private val _selectedParkingPlaceCoordinates by lazy { session.selectedParkingPlaceCoordinates }
    private val selectedParkingPlaceCoordinates = MutableStateFlow<String>(ParkingSchemeConsts.EMPTY_STRING)

    private val _isReservedTransactionPassed = MutableStateFlow(false)
    private val _isCurrentUserReservedParkingPlace by lazy { session.isCurrentUserReservedParkingPlace }
    val isCurrentUserReservedParkingPlace = MutableStateFlow(false)

    val currentUser = MutableResultFlow<User>()

    init {
        appComponent.inject(this)
    }

    fun onOpen() {
        isCurrentUserReservedParkingPlace.value = _isCurrentUserReservedParkingPlace.value
        selectedParkingPlace.value = _selectedParkingPlace.value
        selectedParkingPlaceCoordinates.value = _selectedParkingPlaceCoordinates.value
    }

    fun getParkingScheme(floor: Int) = viewModelScope.launch {
        parkingSchemeResult.loadOrError {
            mapDatabaseRepository.getParkingScheme(currentParkingAddress.value, floor)
        }
    }

    fun isReservedTransactionPassed(): Boolean = _isReservedTransactionPassed.value
    fun setReservedTransactionPassed(isReservedTransactionPassed: Boolean = false) {
        _isReservedTransactionPassed.value = isReservedTransactionPassed
    }

    fun setParkingPlace(name: String, coordinates: String) {
        selectedParkingPlace.value = name
        selectedParkingPlaceCoordinates.value = coordinates
    }

    fun getSelectedParkingPlace(): String = selectedParkingPlace.value

    // Reservation

    fun setParkingPlaceReservation(
        floor: Int
    ) = viewModelScope.launch {
        setParkingPlaceReservationResult.loadOrError(R.string.error_place_reserved) {
            mapDatabaseRepository.setParkingPlaceReservation(
                address = currentParkingAddress.value,
                namePlace = selectedParkingPlace.value,
                floor = floor.toString(),
                placeCoordinates = selectedParkingPlaceCoordinates.value,
                autoNumber = autoNumber.value
            )

            session.changeSelectedParkingPlace(selectedParkingPlace.value)
            session.changeSelectedParkingPlaceCoordinates(selectedParkingPlaceCoordinates.value)
            session.changeIsCurrentUserReservedParkingPlace(true)
            onOpen()
            isCurrentUserReservedParkingPlace.value = true
        }
    }

    fun removeParkingPlaceReservation(
        floor: Int
    ) = viewModelScope.launch {
        removeParkingPlaceReservationResult.loadOrError(R.string.error_place_reserved) {
            mapDatabaseRepository.removeParkingPlaceReservation(
                address = currentParkingAddress.value,
                autoNumber = autoNumber.value,
                floor = floor,
                placeCoordinates = selectedParkingPlaceCoordinates.value
            )

            session.changeSelectedParkingPlace(ParkingSchemeConsts.EMPTY_STRING)
            session.changeSelectedParkingPlaceCoordinates(ParkingSchemeConsts.EMPTY_STRING)
            session.changeIsCurrentUserReservedParkingPlace(false)
            onOpen()
            _isReservedTransactionPassed.value = true
            isCurrentUserReservedParkingPlace.value = false
        }
    }

    fun getCurrentUser() = viewModelScope.launch {
        currentUser.loadOrError {
            authDatabaseRepository.getUserDataFromDatabase()
        }
    }

    fun getParkingPlaceState(namePlace: String, value: Int): ParkingPlaceState = when {
        isCurrentUserReservedParkingPlace.value && namePlace == selectedParkingPlace.value -> ParkingPlaceState.RESERVED_BY_CURRENT_USER
        namePlace != selectedParkingPlace.value && value == 1 -> ParkingPlaceState.RESERVED_BY_OTHER_USERS
        else -> ParkingPlaceState.NOT_RESERVED
    }
}