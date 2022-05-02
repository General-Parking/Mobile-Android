package io.mishkav.generalparking.ui.screens.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.mishkav.generalparking.GeneralParkingApp
import io.mishkav.generalparking.dagger.AppComponent
import io.mishkav.generalparking.domain.entities.ParkingShortInfo
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
    lateinit var session: Session

    val parkingCoordinatesResult = MutableResultFlow<Map<Pair<Double, Double>, String>>()
    val parkingShortInfoResult = MutableResultFlow<Map<String, ParkingShortInfo>>()
    val autoNumberResult = MutableResultFlow<Unit>()
    val currentParkingAddress by lazy { session.currentParkingAddress }

    init {
        appComponent.inject(this)
    }

    fun onOpen() {
        getParkingCoordinates()
        getParkingShortInfo()
        setAutoNumber()
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
}