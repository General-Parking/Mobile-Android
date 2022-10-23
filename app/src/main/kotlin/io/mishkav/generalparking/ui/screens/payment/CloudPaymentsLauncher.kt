package io.mishkav.generalparking.ui.screens.payment

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.cloudpayments.sdk.configuration.CloudpaymentsSDK
import ru.cloudpayments.sdk.configuration.PaymentConfiguration
import ru.cloudpayments.sdk.configuration.PaymentData


class CloudPaymentsLauncher : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(this).get(PaymentViewModel::class.java)
        val balance = viewModel.balanceAmount.value.toInt()
        val paymentAmount = viewModel.paymentAmount.value
        val sum = if (paymentAmount.isNotEmpty())
            balance + paymentAmount.toInt()
        else
            balance

        val cpSdkLauncher = CloudpaymentsSDK.getInstance().launcher(this, result = {
            if (it.status != null) {
                if (it.status == CloudpaymentsSDK.TransactionStatus.Succeeded) {
                    Toast.makeText(
                        this,
                        "Успешно! Транзакция №${it.transactionId}",
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.setGiftBalance(sum)
                    viewModel.changeBalance(sum.toString())
                    finish()
                } else {
                    if (it.reasonCode != 0) {
                        Toast.makeText(
                            this,
                            "Ошибка! Транзакция №${it.transactionId}. Код ошибки ${it.reasonCode}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Ошибка! Транзакция №${it.transactionId}.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    finish()
                }
            } else {
                finish()
            }
        })

        val paymentData = PaymentData(
            "test_api_00000000000000000000002", // PublicId который вы получили в CloudPayments
            paymentAmount, // Сумма транзакции
            currency = "RUB" // Валюта
        )

        val configuration = PaymentConfiguration(
            paymentData,
            null
        )

        cpSdkLauncher.launch(configuration)
    }
}