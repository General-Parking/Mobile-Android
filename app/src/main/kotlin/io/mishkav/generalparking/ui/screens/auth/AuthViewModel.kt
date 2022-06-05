package io.mishkav.generalparking.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.mishkav.generalparking.GeneralParkingApp
import io.mishkav.generalparking.R
import io.mishkav.generalparking.dagger.AppComponent
import io.mishkav.generalparking.data.exceptions.MinSdkVersionException
import io.mishkav.generalparking.data.utils.UserFields.DefaultFields.DEFAULT_STRING_FIELD
import io.mishkav.generalparking.data.utils.UserFields.FIELD_NAME
import io.mishkav.generalparking.data.utils.getMetaUserInfoInstance
import io.mishkav.generalparking.domain.entities.User
import io.mishkav.generalparking.domain.repositories.IAuthRepository
import io.mishkav.generalparking.domain.repositories.IUserDatabaseRepository
import io.mishkav.generalparking.ui.utils.MutableResultFlow
import io.mishkav.generalparking.ui.utils.loadOrError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel(appComponent: AppComponent = GeneralParkingApp.appComponent) : ViewModel() {

    @Inject
    lateinit var authRepository: IAuthRepository

    @Inject
    lateinit var userDatabaseRepository: IUserDatabaseRepository

    val signInResult = MutableResultFlow<Unit>()
    val createNewUserResult = MutableResultFlow<Unit>()
    val resetPasswordResult = MutableResultFlow<Unit>()
    val isEmailVerified = MutableResultFlow<Boolean>()

    private val _currentUser = MutableStateFlow<User>(User.getInstance())
    val currentUser = MutableResultFlow<User>()

    init {
        appComponent.inject(this)
    }

    fun signIn(email: String, password: String) = viewModelScope.launch {
        signInResult.loadOrError(R.string.error_auth) {
            if (!userDatabaseRepository.isMinSdkVersionApproved())
                throw MinSdkVersionException()

            authRepository.signInWithEmailAndPassword(email, password)
        }
    }

    fun createNewUser(name: String, email: String, password: String) = viewModelScope.launch {
        createNewUserResult.loadOrError(R.string.error_registration) {
            if (!userDatabaseRepository.isMinSdkVersionApproved())
                throw MinSdkVersionException()

            authRepository.createUserWithEmailAndPassword(email, password)
            _currentUser.value = _currentUser.value.copy(
                email = email,
                metaUserInfo = getMetaUserInfoInstance(
                    name = name
                )
            )
            userDatabaseRepository.insertUserData(_currentUser.value)
        }
    }

    private fun convertRusToLat(numberAuto: String): String {
        var converted = ""
        numberAuto.forEach { sym ->
            converted += if (sym.isLetter()) {
                when (sym) {
                    'А' -> 'A'
                    'В' -> 'В'
                    'Е' -> 'E'
                    'К' -> 'K'
                    'М' -> 'M'
                    'Н' -> 'H'
                    'О' -> 'O'
                    'Р' -> 'P'
                    'С' -> 'C'
                    'Т' -> 'T'
                    'У' -> 'Y'
                    else -> 'X'
                }
            } else
                sym
        }
        return converted
    }

    fun insertExtensionUserData(
        numberAuto: String,
        carBrand: String,
        phoneNumber: String
    ) = viewModelScope.launch {
        currentUser.loadOrError {
            _currentUser.value = userDatabaseRepository.getUserDataFromDatabase()
            _currentUser.value = _currentUser.value.copy(
                numberAuto = convertRusToLat(numberAuto),
                metaUserInfo = getMetaUserInfoInstance(
                    carBrand = carBrand,
                    name = _currentUser.value.metaUserInfo[FIELD_NAME] ?: DEFAULT_STRING_FIELD,
                    phoneNumber = phoneNumber
                )
            )
            userDatabaseRepository.insertUserData(_currentUser.value)
            _currentUser.value
        }
    }

    fun resetPassword(email: String) = viewModelScope.launch {
        resetPasswordResult.loadOrError {
            if (!userDatabaseRepository.isMinSdkVersionApproved())
                throw MinSdkVersionException()

            authRepository.sendPasswordResetEmail(email)
        }
    }

    fun checkIsEmailVerified() {
        isEmailVerified.loadOrError {
            authRepository.isEmailVerified()
        }
    }
}