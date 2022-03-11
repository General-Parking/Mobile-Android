package io.mishkav.generalparking.ui.screens.map.schemeScreen

import androidx.lifecycle.ViewModel
import io.mishkav.generalparking.GeneralParkingApp
import io.mishkav.generalparking.dagger.AppComponent
import io.mishkav.generalparking.domain.repositories.IMapDatabaseRepository
import io.mishkav.generalparking.state.Session
import javax.inject.Inject

class SchemeViewModel(appComponent: AppComponent = GeneralParkingApp.appComponent) : ViewModel() {
    @Inject
    lateinit var mapDatabaseRepository: IMapDatabaseRepository

    @Inject
    lateinit var session: Session

    val currentParkingAddress by lazy { session.currentParkingAddress }

    init {
        appComponent.inject(this)
    }
}