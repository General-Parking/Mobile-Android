package io.mishkav.generalparking.ui.screens.map

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.mishkav.generalparking.GeneralParkingApp
import io.mishkav.generalparking.R
import io.mishkav.generalparking.dagger.AppComponent
import io.mishkav.generalparking.data.repositories.MapDatabaseRepository
import io.mishkav.generalparking.domain.entities.LoadState
import io.mishkav.generalparking.domain.entities.ParkingShortInfo
import io.mishkav.generalparking.domain.entities.TimeCallback
import io.mishkav.generalparking.domain.entities.UserState
import io.mishkav.generalparking.domain.repositories.IAuthDatabaseRepository
import io.mishkav.generalparking.domain.repositories.IMapDatabaseRepository
import io.mishkav.generalparking.state.Session
import io.mishkav.generalparking.ui.utils.MutableResultFlow
import io.mishkav.generalparking.ui.utils.loadOrError
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapViewModel(appComponent: AppComponent = GeneralParkingApp.appComponent) : ViewModel() {

    @Inject
    lateinit var mapDatabaseRepository: IMapDatabaseRepository

    @Inject
    lateinit var authDatabaseRepository: IAuthDatabaseRepository

    @Inject
    lateinit var session: Session

    val parkingCoordinatesResult = MutableResultFlow<Map<Pair<Double, Double>, String>>()
    val reservationAddressResult = MutableResultFlow<String>()
    val timeReservationResult = MutableResultFlow<Unit>()
    var timeReservation = mutableStateOf("")
    val bookingTimeResult = MutableResultFlow<Long>()
    val timeExitResult = MutableResultFlow<String>()
    val bookingRatioResult = MutableResultFlow<Double>()
    val timeArriveResult = MutableResultFlow<Unit>()
    var timeArrive = mutableStateOf("")
    val isArrivedResult = MutableResultFlow<Unit>()
    val resetIsArrivedResult = MutableResultFlow<LoadState>()
    val isExitResult = MutableResultFlow<Unit>()
    val resetIsExitResult = MutableResultFlow<LoadState>()
    val parkingShortInfoResult = MutableResultFlow<Map<String, ParkingShortInfo>>()
    val autoNumberResult = MutableResultFlow<Unit>()
    val isMinSdkVersionApproved = MutableResultFlow<Boolean>()
    val currentParkingAddress by lazy { session.currentParkingAddress }
    val selectedParkingPlace by lazy { session.selectedParkingPlaceName }
    val userState by lazy { session.userState }
    val isArrived by lazy { session.isArrived }
    val isExit by lazy { session.isExit }
    private val removeParkingPlaceReservationResult = MutableResultFlow<Unit>()
    private val autoNumber by lazy { session.autoNumber }
    private val selectedParkingPlaceCoordinates by lazy { session.selectedParkingPlaceCoordinates }
    private val _selectedParkingPlaceFloor by lazy { session.selectedParkingPlaceFloor }

    init {
        appComponent.inject(this)
    }

    fun onOpen() {
        getMinSdkApprove()
        getParkingCoordinates()
        getParkingShortInfo()
        getReservationAddress()
        setAutoNumber()
        getBookingTime()
        getBookingRatio()
        getTimeArrive()
        getIsArrived()
        getIsExit()
        getTimeExit()
        getTimeReservation()
    }

    fun removeParkingPlaceReservation() = viewModelScope.launch {
        removeParkingPlaceReservationResult.loadOrError(R.string.error_place_reserved) {
            mapDatabaseRepository.removeParkingPlaceReservation(
                address = currentParkingAddress.value,
                autoNumber = autoNumber.value,
                floor = _selectedParkingPlaceFloor.value.toInt(),
                placeCoordinates = selectedParkingPlaceCoordinates.value
            )

            session.changeSelectedParkingPlaceName(EMPTY_STRING)
            session.changeSelectedParkingPlaceCoordinates(EMPTY_STRING)
            session.changeSelectedParkingPlaceFloor(EMPTY_STRING)
        }
    }

    fun getMinSdkApprove() = viewModelScope.launch {
        isMinSdkVersionApproved.loadOrError {
            authDatabaseRepository.isMinSdkVersionApproved()
        }
    }

    fun setCurrentParkingAddress(address: String) {
        session.changeCurrentParkingAddress(address)
    }

    fun setAutoNumber() = viewModelScope.launch {
        autoNumberResult.loadOrError {
            val autoNumber = mapDatabaseRepository.getAutoNumber()
            session.changeAutoNumber(autoNumber)
        }
    }

    fun getTimeReservation() = viewModelScope.launch {
        timeReservationResult.loadOrError {
            mapDatabaseRepository.getTimeReservation(object : TimeCallback {
                override fun onCallback(value: String) {
                    timeReservation.value = value

                    if (timeReservation.value == EMPTY_STRING)
                        session.changeUserState(UserState.NOTHING.value)
                    else if (timeReservation.value != EMPTY_STRING && timeArrive.value == EMPTY_STRING)
                        session.changeUserState(UserState.RESERVED.value)
                    else if (timeArrive.value != EMPTY_STRING)
                        session.changeUserState(UserState.ARRIVED.value)
                }
            })
        }
    }

    fun getBookingTime() = viewModelScope.launch {
        bookingTimeResult.loadOrError {
            mapDatabaseRepository.getBookingTime()
        }
    }

    fun getTimeExit() = viewModelScope.launch {
        timeExitResult.loadOrError {
            mapDatabaseRepository.getTimeExit()
        }
    }

    fun getBookingRatio() = viewModelScope.launch {
        bookingRatioResult.loadOrError {
            mapDatabaseRepository.getBookingRatio(
                address = currentParkingAddress.value,
                floor = _selectedParkingPlaceFloor.value
            )
        }
    }

    fun getTimeArrive() = viewModelScope.launch {
        timeArriveResult.loadOrError {
            mapDatabaseRepository.getTimeArrive(object : TimeCallback {
                override fun onCallback(value: String) {
                    timeArrive.value = value

                    if (timeReservation.value == EMPTY_STRING)
                        session.changeUserState(UserState.NOTHING.value)
                    else if (timeReservation.value != EMPTY_STRING && timeArrive.value == EMPTY_STRING)
                        session.changeUserState(UserState.RESERVED.value)
                    else if (timeArrive.value != EMPTY_STRING)
                        session.changeUserState(UserState.ARRIVED.value)
                }
            })
        }
    }

    fun getIsArrived() = viewModelScope.launch {
        isArrivedResult.loadOrError {
            mapDatabaseRepository.getIsArrived()
        }
    }

    fun resetIsArrived() = viewModelScope.launch {
        resetIsArrivedResult.loadOrError {
            LoadState.LOADING
            mapDatabaseRepository.resetIsArrived()
            LoadState.RESETING
        }
    }

    fun getIsExit() = viewModelScope.launch {
        isExitResult.loadOrError {
            mapDatabaseRepository.getIsExit()
        }
    }

    fun resetIsExit() = viewModelScope.launch {
        resetIsExitResult.loadOrError {
            session.changeUserState(EMPTY_STRING)
            LoadState.LOADING
            mapDatabaseRepository.resetIsExit()
            LoadState.RESETING
        }
    }

    fun getParkingCoordinates() = viewModelScope.launch {
        parkingCoordinatesResult.loadOrError {
            val rawCoordinates = mapDatabaseRepository.getParkingCoordinates()

            val coordinatesMap = mutableMapOf<Pair<Double, Double>, String>()

            for ((key, value) in rawCoordinates) {
                val rawCoordinates = key.split("-")
                val coordinates = Pair(
                    rawCoordinates[0].replace(",", ".").toDouble(),
                    rawCoordinates[1].replace(",", ".").toDouble()
                )
                coordinatesMap[coordinates] = value
            }

            coordinatesMap
        }
    }

    fun getParkingShortInfo() = viewModelScope.launch {
        parkingShortInfoResult.loadOrError {
            mapDatabaseRepository.getParkingShortInfo()
        }
    }

    fun getReservationAddress() = viewModelScope.launch {
        reservationAddressResult.loadOrError {
            mapDatabaseRepository.getReservationAddress()
        }
    }

    private companion object {
        const val EMPTY_STRING = ""
    }
}