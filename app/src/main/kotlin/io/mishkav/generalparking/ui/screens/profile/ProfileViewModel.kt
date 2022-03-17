package io.mishkav.generalparking.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.mishkav.generalparking.GeneralParkingApp
import io.mishkav.generalparking.R
import io.mishkav.generalparking.dagger.AppComponent
import io.mishkav.generalparking.domain.repositories.IAuthRepository
import io.mishkav.generalparking.ui.utils.MutableResultFlow
import io.mishkav.generalparking.ui.utils.loadOrError
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel(appComponent: AppComponent = GeneralParkingApp.appComponent) : ViewModel() {

    @Inject
    lateinit var authRepository: IAuthRepository

    val signOutResult = MutableResultFlow<Unit>()

    fun signOut() = viewModelScope.launch {
        signOutResult.loadOrError(R.string.error_auth) {
            authRepository.signOut()
        }
    }

    init {
        appComponent.inject(this)
    }
}