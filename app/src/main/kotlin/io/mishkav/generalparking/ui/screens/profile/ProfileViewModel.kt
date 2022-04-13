package io.mishkav.generalparking.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.mishkav.generalparking.GeneralParkingApp
import io.mishkav.generalparking.R
import io.mishkav.generalparking.dagger.AppComponent
import io.mishkav.generalparking.domain.entities.User
import io.mishkav.generalparking.domain.repositories.IAuthDatabaseRepository
import io.mishkav.generalparking.domain.repositories.IAuthRepository
import io.mishkav.generalparking.ui.utils.MutableResultFlow
import io.mishkav.generalparking.ui.utils.loadOrError
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel(appComponent: AppComponent = GeneralParkingApp.appComponent) : ViewModel() {

    @Inject
    lateinit var authRepository: IAuthRepository

    @Inject
    lateinit var authDatabaseRepository: IAuthDatabaseRepository

    val currentUser = MutableResultFlow<User>()
    val signOutResult = MutableResultFlow<Unit>()

    init {
        appComponent.inject(this)
    }

    fun onOpen() {
        getUserDataFromDatabase()
    }

    fun getUserDataFromDatabase() = viewModelScope.launch {
        currentUser.loadOrError {
            authDatabaseRepository.getUserDataFromDatabase()
        }
    }

    fun signOut() = viewModelScope.launch {
        signOutResult.loadOrError(R.string.error_auth) {
            authRepository.signOut()
        }
    }
}