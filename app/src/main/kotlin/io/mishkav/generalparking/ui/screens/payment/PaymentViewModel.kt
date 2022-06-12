package io.mishkav.generalparking.ui.screens.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.mishkav.generalparking.GeneralParkingApp
import io.mishkav.generalparking.dagger.AppComponent
import io.mishkav.generalparking.domain.repositories.IUserDatabaseRepository
import io.mishkav.generalparking.ui.screens.payment.config.PaymentConfig
import io.mishkav.generalparking.ui.screens.payment.config.PaymentMethods
import io.mishkav.generalparking.ui.utils.MutableResultFlow
import io.mishkav.generalparking.ui.utils.loadOrError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class PaymentViewModel(appComponent: AppComponent = GeneralParkingApp.appComponent) : ViewModel() {

    @Inject
    lateinit var userDatabaseRepository: IUserDatabaseRepository

    val balance = MutableResultFlow<Int>()
    val giftResult = MutableResultFlow<Unit>()

    val selectedOption by lazy { userDatabaseRepository.selectedOption}

    init {
        appComponent.inject(this)
    }

    fun onOpen() {
        getUserBalance()
    }

    fun changeSelected(method: PaymentMethods) {
        userDatabaseRepository.changeSelected(method)
    }

    private fun getUserBalance() = viewModelScope.launch {
        balance.loadOrError {
            userDatabaseRepository.getUserBalance()
        }
    }

    fun setGiftBalance(newBalance: Int) = viewModelScope.launch {
        giftResult.loadOrError {
            userDatabaseRepository.setGiftBalance(newBalance)
        }
    }
}