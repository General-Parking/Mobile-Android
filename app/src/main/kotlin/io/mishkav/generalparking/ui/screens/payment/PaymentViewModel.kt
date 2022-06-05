package io.mishkav.generalparking.ui.screens.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.mishkav.generalparking.GeneralParkingApp
import io.mishkav.generalparking.dagger.AppComponent
import io.mishkav.generalparking.domain.repositories.IUserDatabaseRepository
import io.mishkav.generalparking.ui.utils.MutableResultFlow
import io.mishkav.generalparking.ui.utils.loadOrError
import kotlinx.coroutines.launch
import javax.inject.Inject

class PaymentViewModel(appComponent: AppComponent = GeneralParkingApp.appComponent) : ViewModel() {

    @Inject
    lateinit var userDatabaseRepository: IUserDatabaseRepository

    val balance = MutableResultFlow<Int>()

    init {
        appComponent.inject(this)
    }

    fun onOpen() {
        getUserBalance()
    }

    private fun getUserBalance() = viewModelScope.launch {
        balance.loadOrError {
            userDatabaseRepository.getUserBalance()
        }
    }
}