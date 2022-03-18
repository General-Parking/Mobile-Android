package io.mishkav.generalparking.ui.screens.map.schemeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.mishkav.generalparking.GeneralParkingApp
import io.mishkav.generalparking.R
import io.mishkav.generalparking.dagger.AppComponent
import io.mishkav.generalparking.domain.entities.ParkingScheme
import io.mishkav.generalparking.domain.repositories.IMapDatabaseRepository
import io.mishkav.generalparking.state.Session
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

    val parkingSchemeResult = MutableResultFlow<ParkingScheme>()
    val setParkingPlaceReservationResult = MutableResultFlow<Unit>()
    val removeParkingPlaceReservationResult = MutableResultFlow<Unit>()

    val currentParkingAddress by lazy { session.currentParkingAddress }
    private val autoNumber by lazy { session.autoNumber }

    private val _selectedParkingPlace by lazy { session.selectedParkingPlace }
    val selectedParkingPlace = MutableStateFlow<String>(ParkingSchemeConsts.EMPTY_STRING)

    private val _selectedParkingPlaceCoordinates by lazy { session.selectedParkingPlaceCoordinates }
    private val selectedParkingPlaceCoordinates = MutableStateFlow<String>(ParkingSchemeConsts.EMPTY_STRING)

    val isPlaceParkingSelected = MutableStateFlow(selectedParkingPlace.value.isEmpty())

    init {
        appComponent.inject(this)
    }

    fun onOpen() {
        selectedParkingPlace.value = _selectedParkingPlace.value
        selectedParkingPlaceCoordinates.value = _selectedParkingPlaceCoordinates.value
    }

    fun getParkingScheme(floor: Int) = viewModelScope.launch {
        parkingSchemeResult.loadOrError {
            mapDatabaseRepository.getParkingScheme(currentParkingAddress.value, floor)
        }
    }

    fun isGivenPlaceSelected(namePlace: String): Boolean {
        return isPlaceParkingSelected.value && namePlace == selectedParkingPlace.value
    }

    fun setParkingPlace(name: String, coordinates: String) {
        selectedParkingPlace.value = name
        selectedParkingPlaceCoordinates.value = coordinates
    }

    fun getSelectedParkingPlace(): String = selectedParkingPlace.value

    fun setIsPlaceParkingSelected(isSelected: Boolean) {
        isPlaceParkingSelected.value = isSelected
    }

    // Reservation

    fun setParkingPlaceReservation(
        floor: Int
    ) = viewModelScope.launch {
        setParkingPlaceReservationResult.loadOrError(R.string.error_place_reserved) {
            mapDatabaseRepository.setParkingPlaceReservation(
                address = currentParkingAddress.value,
                namePlace = selectedParkingPlace.value,
                floor = floor,
                placeCoordinates = selectedParkingPlaceCoordinates.value,
                autoNumber = autoNumber.value
            )
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
            isPlaceParkingSelected.value = false
            selectedParkingPlace.value = ParkingSchemeConsts.EMPTY_STRING
            selectedParkingPlaceCoordinates.value = ParkingSchemeConsts.EMPTY_STRING
        }
    }

    fun setParkingPlaceDataToSession() {
        session.changeSelectedParkingPlace(selectedParkingPlace.value)
        session.changeSelectedParkingPlaceCoordinates(selectedParkingPlaceCoordinates.value)
    }
}