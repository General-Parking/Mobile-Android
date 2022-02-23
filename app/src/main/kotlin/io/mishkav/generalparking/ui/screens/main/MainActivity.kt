package io.mishkav.generalparking.ui.screens.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.mishkav.generalparking.ui.screens.auth.authorization.AuthorizationScreen
import io.mishkav.generalparking.ui.screens.auth.confirmEmail.ConfirmEmailScreen
import io.mishkav.generalparking.ui.screens.auth.forgotPassword.ForgotPasswordScreen
import io.mishkav.generalparking.ui.screens.auth.registration.RegistrationScreen
import io.mishkav.generalparking.ui.screens.auth.registrationExtensionData.RegistrationExtensionData
import io.mishkav.generalparking.ui.theme.GeneralParkingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val viewModel: MainViewModel = viewModel()

            GeneralParkingTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen(
                        viewModel = viewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    // val isAuthorized by viewModel.isAuthorized.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.authorization
    ) {
        composable(Routes.authorization) {
            AuthorizationScreen(
                navController = navController
            )
        }

        composable(Routes.confirmEmail) {
            ConfirmEmailScreen(
                navController = navController
            )
        }

        composable(Routes.forgotPassword) {
            ForgotPasswordScreen(
                navController = navController
            )
        }

        composable(Routes.registration) {
            RegistrationScreen(
                navController = navController
            )
        }

        composable(Routes.registrationExtensionData) {
            RegistrationExtensionData(
                navController = navController
            )
        }
    }
}

object Routes {
    const val authorization = "authorization"
    const val confirmEmail = "confirmEmail"
    const val forgotPassword = "forgotPassword"
    const val registration = "registration"
    const val registrationExtensionData = "registrationExtensionData"
}
