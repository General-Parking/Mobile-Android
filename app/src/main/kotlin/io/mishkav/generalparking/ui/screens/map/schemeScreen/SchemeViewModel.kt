package io.mishkav.generalparking.ui.screens.map.schemeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.mishkav.generalparking.GeneralParkingApp
import io.mishkav.generalparking.dagger.AppComponent
import io.mishkav.generalparking.domain.entities.ParkingScheme
import io.mishkav.generalparking.domain.repositories.IMapDatabaseRepository
import io.mishkav.generalparking.state.Session
import io.mishkav.generalparking.ui.utils.MutableResultFlow
import io.mishkav.generalparking.ui.utils.loadOrError
import kotlinx.coroutines.launch
import javax.inject.Inject

class SchemeViewModel(appComponent: AppComponent = GeneralParkingApp.appComponent) : ViewModel() {
    @Inject
    lateinit var mapDatabaseRepository: IMapDatabaseRepository

    @Inject
    lateinit var session: Session

    val parkingSchemeResult = MutableResultFlow<ParkingScheme>()
    val currentParkingAddress by lazy { session.currentParkingAddress }

    init {
        appComponent.inject(this)
    }

    fun getParkingScheme(floor: Int) = viewModelScope.launch {
        parkingSchemeResult.loadOrError {
            mapDatabaseRepository.getParkingScheme(currentParkingAddress.value, floor)
        }
    }
}