package io.mishkav.generalparking.ui.screens.auth.authorization

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import io.mishkav.generalparking.ui.screens.auth.AuthViewModel
import io.mishkav.generalparking.ui.utils.BiometricAuth


class BiometricPrompt : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = AuthViewModel()

        setContent {

            BiometricAuth.authenticate(
                this,
                title = "Biometric Authentication",
                subtitle = "Authenticate to proceed",
                negativeText = "Cancel",
                onSuccess = {

                    runOnUiThread {

                        Toast.makeText(
                            this,
                            "Authenticated successfully",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                        viewModel.changeGetSavedParams("true")

                        finish()
                    }
                },
                onError = { errorString ->
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Authentication error: $errorString",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                    finish()
                },
                onFailed = {
                    runOnUiThread {
                        Toast.makeText(
                            this,
                            "Authentication failed",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                    finish()
                }
            )
        }
    }
}