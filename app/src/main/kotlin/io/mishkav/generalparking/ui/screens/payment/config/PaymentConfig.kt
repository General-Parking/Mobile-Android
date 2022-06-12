package io.mishkav.generalparking.ui.screens.payment.config

import androidx.annotation.DrawableRes
import io.mishkav.generalparking.R

class PaymentMethods(
    val title: String,
    val description: String,
    @DrawableRes val icon: Int
)

object PaymentConfig {

    // Hardcode, but need because of extension and requirements...
    val paymentMethods = arrayListOf(
        PaymentMethods(
            title = "Бесплатно",
            description = "Коммисия 0%, минимум 0 ₽",
            icon = R.drawable.ic_gift
        )
    )
}