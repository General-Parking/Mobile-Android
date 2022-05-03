package io.mishkav.generalparking.ui.screens.scheme

import android.util.Log
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
import io.mishkav.generalparking.ui.screens.scheme.components.NotSelectedPlaceState
import io.mishkav.generalparking.ui.screens.scheme.components.ReservedPlaceState
import io.mishkav.generalparking.ui.screens.scheme.components.SchemeState
import io.mishkav.generalparking.ui.screens.scheme.components.equal
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
    private val _selectedParkingPlaceName by lazy { session.selectedParkingPlaceName }
    private val _selectedParkingPlaceCoordinates by lazy { session.selectedParkingPlaceCoordinates }

    val currentUser = MutableResultFlow<User>()
    val parkingSchemeState: MutableStateFlow<SchemeState> = MutableStateFlow(
        NotSelectedPlaceState()
    )

    init {
        appComponent.inject(this)
    }

    fun setParkingSchemeState(state: SchemeState) {
        when {
            parkingSchemeState.value !is ReservedPlaceState && parkingSchemeState.value.equal(state) -> {
                parkingSchemeState.value = NotSelectedPlaceState()
            }
            parkingSchemeState.value !is ReservedPlaceState -> {
                parkingSchemeState.value = state
            }
        }
    }

    fun getParkingScheme() = viewModelScope.launch {
        parkingSchemeResult.loadOrError {
            mapDatabaseRepository.getParkingScheme(currentParkingAddress.value)
        }
    }

    fun setParkingPlaceReservation(
        floor: Int
    ) = viewModelScope.launch {
        setParkingPlaceReservationResult.loadOrError(R.string.error_place_reserved) {
            mapDatabaseRepository.setParkingPlaceReservation(
                address = currentParkingAddress.value,
                namePlace = parkingSchemeState.value.name,
                floor = floor.toString(),
                placeCoordinates = parkingSchemeState.value.coordinates,
                autoNumber = autoNumber.value
            )

            parkingSchemeState.value = ReservedPlaceState(
                name = parkingSchemeState.value.name,
                coordinates = parkingSchemeState.value.coordinates,
                floor = floor,
                address = currentParkingAddress.value
            )
            session.changeSelectedParkingPlaceName(parkingSchemeState.value.name)
            session.changeSelectedParkingPlaceCoordinates(parkingSchemeState.value.coordinates)
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
                placeCoordinates = parkingSchemeState.value.coordinates
            )
            parkingSchemeResult.value.data?.get(floor.toString())?.places?.get(parkingSchemeState.value.coordinates)?.let {
                it.value = 0
            }
            parkingSchemeState.value = NotSelectedPlaceState()
            session.changeSelectedParkingPlaceName(EMPTY_STRING)
            session.changeSelectedParkingPlaceCoordinates(EMPTY_STRING)
        }
    }

    fun getCurrentUser() = viewModelScope.launch {
        currentUser.loadOrError {
            authDatabaseRepository.getUserDataFromDatabase()
        }
    }

    val onOpenResult = MutableResultFlow<Unit>()
    fun onOpenTest() = viewModelScope.launch {
        onOpenResult.loadOrError {
            val scheme = mapDatabaseRepository.getParkingScheme(currentParkingAddress.value)
            val user = authDatabaseRepository.getUserDataFromDatabase()

            parkingSchemeState.value = if (user.reservationAddress.isNotEmpty()) {

                val coordinates =
                    scheme[user.reservationLevel]?.places?.filter { it.value.name == user.reservationPlace }?.keys.orEmpty()

                ReservedPlaceState(
                    name = user.reservationPlace,
                    coordinates = coordinates.first(),
                    floor = user.reservationLevel.toInt(),
                    address = currentParkingAddress.value
                )
            } else
                NotSelectedPlaceState()

            parkingSchemeResult.value.data = scheme
            currentUser.value.data = user
        }
    }

    private companion object {
        const val EMPTY_STRING = ""
    }
}