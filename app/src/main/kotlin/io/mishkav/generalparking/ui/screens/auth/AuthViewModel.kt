package io.mishkav.generalparking.ui.screens.auth

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

class AuthViewModel(appComponent: AppComponent = GeneralParkingApp.appComponent) : ViewModel() {
    @Inject
    lateinit var authRepository: IAuthRepository

    val signInResult = MutableResultFlow<Unit>()
    val createNewUserResult = MutableResultFlow<Unit>()
    val resetPasswordResult = MutableResultFlow<Unit>()

    init {
        appComponent.inject(this)
    }

    fun signIn(email: String, password: String) = viewModelScope.launch {
        signInResult.loadOrError(R.string.error_auth) {
            authRepository.signInWithEmailAndPassword(email, password)
        }
    }

    fun createNewUser(email: String, password: String) = viewModelScope.launch {
        createNewUserResult.loadOrError(R.string.error_registration) {
            authRepository.createUserWithEmailAndPassword(email, password)
            //Вставка данных в бд
        }
    }

    fun resetPassword(email: String) = viewModelScope.launch {
        resetPasswordResult.loadOrError {
            authRepository.sendPasswordResetEmail(email)
        }
    }
}