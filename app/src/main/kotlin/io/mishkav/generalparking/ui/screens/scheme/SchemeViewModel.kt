package io.mishkav.generalparking.ui.screens.scheme

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
import io.mishkav.generalparking.ui.screens.scheme.components.NotSelectedState
import io.mishkav.generalparking.ui.screens.scheme.components.SchemeState
import io.mishkav.generalparking.ui.screens.scheme.components.SelectedPlace
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

    val parkingSchemeResult = MutableResultFlow<Map<String, ParkingScheme>>()
    val setParkingPlaceReservationResult = MutableResultFlow<Unit>()
    val removeParkingPlaceReservationResult = MutableResultFlow<Unit>()

    val currentParkingAddress by lazy { session.currentParkingAddress }
    private val autoNumber by lazy { session.autoNumber }

    private val _selectedParkingPlace by lazy { session.selectedParkingPlace }
    val selectedParkingPlace = MutableStateFlow<String>("")

    private val _selectedParkingPlaceCoordinates by lazy { session.selectedParkingPlaceCoordinates }
    private val selectedParkingPlaceCoordinates = MutableStateFlow<String>("")

    private val _isReservedTransactionPassed = MutableStateFlow(false)
    private val _isCurrentUserReservedParkingPlace by lazy { session.isCurrentUserReservedParkingPlace }
    val isCurrentUserReservedParkingPlace = MutableStateFlow(false)

    val currentUser = MutableResultFlow<User>()

    //

    val parkingSchemeState: MutableStateFlow<SchemeState> = MutableStateFlow(NotSelectedState())

    init {
        appComponent.inject(this)
    }

    fun setParkingSchemeState(state: SchemeState) {
        parkingSchemeState.value = state
    }

    fun onOpen() {
        isCurrentUserReservedParkingPlace.value = _isCurrentUserReservedParkingPlace.value
        selectedParkingPlace.value = _selectedParkingPlace.value
        selectedParkingPlaceCoordinates.value = _selectedParkingPlaceCoordinates.value
    }

    fun getParkingScheme() = viewModelScope.launch {
        parkingSchemeResult.loadOrError {
            mapDatabaseRepository.getParkingScheme(currentParkingAddress.value)
        }
    }

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

            session.changeSelectedParkingPlace("")
            session.changeSelectedParkingPlaceCoordinates("")
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
}