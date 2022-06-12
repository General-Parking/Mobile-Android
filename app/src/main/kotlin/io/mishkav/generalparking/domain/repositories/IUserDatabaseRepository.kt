package io.mishkav.generalparking.domain.repositories

import io.mishkav.generalparking.domain.entities.User
import io.mishkav.generalparking.ui.screens.payment.config.PaymentMethods
import kotlinx.coroutines.flow.StateFlow

interface IUserDatabaseRepository {
    val selectedOption: StateFlow<PaymentMethods>
    fun changeSelected(method: PaymentMethods)

    suspend fun insertUserData(user: User)
    suspend fun getUserDataFromDatabase(): User
    suspend fun getUserBalance(): Int
    suspend fun isMinSdkVersionApproved(): Boolean
    suspend fun setGiftBalance(balance: Int)
}