package io.mishkav.generalparking.ui.screens.main

import androidx.lifecycle.ViewModel
import io.mishkav.generalparking.GeneralParkingApp
import io.mishkav.generalparking.dagger.AppComponent
import io.mishkav.generalparking.domain.repositories.IAuthRepository
import javax.inject.Inject

class MainViewModel(appComponent: AppComponent = GeneralParkingApp.appComponent) : ViewModel() {

    @Inject
    lateinit var authRepository: IAuthRepository

    val isAuthorized by lazy { authRepository.isUserAuthorized() }

    init {
        appComponent.inject(this)
    }
}