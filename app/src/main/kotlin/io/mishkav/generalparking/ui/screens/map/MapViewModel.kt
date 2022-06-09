package io.mishkav.generalparking.ui.screens.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.mishkav.generalparking.GeneralParkingApp
import io.mishkav.generalparking.R
import io.mishkav.generalparking.dagger.AppComponent
import io.mishkav.generalparking.domain.entities.ParkingShortInfo
import io.mishkav.generalparking.domain.entities.UserState
import io.mishkav.generalparking.domain.repositories.IAuthDatabaseRepository
import io.mishkav.generalparking.domain.repositories.IMapDatabaseRepository
import io.mishkav.generalparking.state.Session
import io.mishkav.generalparking.ui.utils.MutableResultFlow
import io.mishkav.generalparking.ui.utils.loadOrError
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

class MapViewModel(appComponent: AppComponent = GeneralParkingApp.appComponent) : ViewModel() {

    @Inject
    lateinit var mapDatabaseRepository: IMapDatabaseRepository

    @Inject
    lateinit var authDatabaseRepository: IAuthDatabaseRepository

    @Inject
    lateinit var session: Session

    // Fields about parking
    val parkingCoordinatesResult = MutableResultFlow<Map<Pair<Double, Double>, String>>()
    val reservationAddressResult = MutableResultFlow<String>()
    val parkingShortInfoResult = MutableResultFlow<Map<String, ParkingShortInfo>>()
    val autoNumberResult = MutableResultFlow<Unit>()
    val currentParkingAddress by lazy { session.currentParkingAddress }
    val selectedParkingPlace by lazy { session.selectedParkingPlaceName }
    private val removeParkingPlaceReservationResult = MutableResultFlow<Unit>()
    private val autoNumber by lazy { session.autoNumber }
    private val selectedParkingPlaceCoordinates by lazy { session.selectedParkingPlaceCoordinates }
    private val selectedParkingPlaceFloor by lazy { session.selectedParkingPlaceFloor }

    // Fields for userStates
    val timeReservationResult = MutableResultFlow<Unit>()
    val timeReservation by lazy { mapDatabaseRepository.timeReservation }
    val bookingTimeResult = MutableResultFlow<Long>()
    val timeExitResult = MutableResultFlow<String>()
    val bookingRatioResult = MutableResultFlow<Double>()
    val timeArriveResult = MutableResultFlow<Unit>()
    val timeArrive by lazy { mapDatabaseRepository.timeArrive }
    val onTimerResult = MutableResultFlow<Unit>()

    // Fields for alertStates
    val isAlertResult = MutableResultFlow<Unit>()
    val resetAlertResult = MutableResultFlow<Unit>()

    // States
    val userState by lazy {  mapDatabaseRepository.userState }
    val alertState by lazy {  mapDatabaseRepository.alertState }

    val isMinSdkVersionApproved = MutableResultFlow<Boolean>()

    init {
        appComponent.inject(this)
    }

    fun onOpen() {
        getMinSdkApprove()
        getParkingCoordinates()
        getParkingShortInfo()
        getReservationAddress()
        setAutoNumber()
        getBookingRatio()
        getTimeArrive()
        getAlertState()
        getTimeExit()
        loadData()
    }

    private fun loadData() = viewModelScope.launch {
        onTimerResult.loadOrError {
            val jobsToLoad = arrayOf(
                launch {
                    timeReservationResult.loadOrError {
                        mapDatabaseRepository.getTimeReservation()
                    }
                },
                launch {
                    bookingTimeResult.loadOrError {
                        mapDatabaseRepository.getBookingTime()
                    }
                }
            )

            joinAll(*jobsToLoad)
        }
    }

    fun removeParkingPlaceReservation() = viewModelScope.launch {
        removeParkingPlaceReservationResult.loadOrError(R.string.error_place_reserved) {
            mapDatabaseRepository.removeParkingPlaceReservation(
                address = currentParkingAddress.value,
                autoNumber = autoNumber.value,
                floor = selectedParkingPlaceFloor.value.toInt(),
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

    fun getTimeExit() = viewModelScope.launch {
        timeExitResult.loadOrError {
            mapDatabaseRepository.getTimeExit()
        }
    }

    fun getBookingRatio() = viewModelScope.launch {
        bookingRatioResult.loadOrError {
            mapDatabaseRepository.getBookingRatio(
                address = currentParkingAddress.value,
                floor = selectedParkingPlaceFloor.value
            )
        }
    }

    fun getTimeArrive() = viewModelScope.launch {
        timeArriveResult.loadOrError {
            mapDatabaseRepository.getTimeArrive()
        }
    }

    fun getAlertState() = viewModelScope.launch {
        isAlertResult.loadOrError {
            mapDatabaseRepository.getAlertState()
        }
    }

    fun resetAlertState(state: UserState) = viewModelScope.launch {
        resetAlertResult.loadOrError {
            if (state == UserState.ARRIVED) {
                mapDatabaseRepository.changeUserState(UserState.ARRIVED)
                mapDatabaseRepository.resetIsArrived()
            }
            else if (state == UserState.EXIT) {
                mapDatabaseRepository.changeUserState(UserState.NOT_RESERVED)
                mapDatabaseRepository.resetIsExit()
            }
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