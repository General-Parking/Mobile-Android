package io.mishkav.generalparking.ui.screens.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Surface
import androidx.compose.material.contentColorFor
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import io.mishkav.generalparking.ui.screens.auth.authorization.AuthorizationScreen
import io.mishkav.generalparking.ui.screens.auth.confirmEmail.ConfirmEmailScreen
import io.mishkav.generalparking.ui.screens.auth.forgotPassword.ForgotPasswordScreen
import io.mishkav.generalparking.ui.screens.auth.registration.RegistrationScreen
import io.mishkav.generalparking.ui.screens.auth.registrationExtensionData.RegistrationExtensionData
import io.mishkav.generalparking.ui.screens.map.mapScreen.MapScreen
import io.mishkav.generalparking.ui.theme.GeneralParkingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                val viewModel: MainViewModel = viewModel()

                GeneralParkingTheme() {
                    ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
                        Scaffold(
                            scaffoldState = scaffoldState,
                            snackbarHost = {
                                SnackbarHost(
                                    hostState = it,
                                    modifier = Modifier.navigationBarsPadding()
                                ) {
                                    Snackbar(
                                        snackbarData = it,
                                        backgroundColor = MaterialTheme.colorScheme.surface,
                                        contentColor = MaterialTheme.colorScheme.onPrimary,
                                        shape = shapes.medium
                                    )
                                }
                            },
                            content = {
                                MainScreen(
                                    viewModel = viewModel,
                                    navController = navController,
                                    scaffoldState = scaffoldState,
                                    paddingValues = it
                                )
                            }
                        )
                    }
            }
        }
    }
}

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    paddingValues: PaddingValues
) {
    val onError: @Composable (Int) -> Unit = { message ->
        val strMessage = stringResource(message)
        LaunchedEffect(Unit) {
            scaffoldState.snackbarHostState.showSnackbar(strMessage)
        }
    }

    // val isAuthorized by viewModel.isAuthorized.collectAsState()
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        NavHost(
            navController = navController,
            startDestination = Routes.authorization
        ) {
            composable(Routes.authorization) {
                AuthorizationScreen(
                    navController = navController,
                    onError = onError
                )
            }

            composable(Routes.confirmEmail) {
                ConfirmEmailScreen(
                    navController = navController,
                    onError = onError
                )
            }

            composable(Routes.forgotPassword) {
                ForgotPasswordScreen(
                    navController = navController,
                    onShowMessage = onError
                )
            }

            composable(Routes.registration) {
                RegistrationScreen(
                    navController = navController,
                    onError = onError
                )
            }

            composable(Routes.registrationExtensionData) {
                RegistrationExtensionData(
                    navController = navController,
                    onError = onError
                )
            }

            composable(Routes.map) {
                MapScreen(
                    navController = navController,
                    onError = onError
                )
            }
        }
    }
}

object Routes {
    const val authorization = "authorization"
    const val confirmEmail = "confirmEmail"
    const val forgotPassword = "forgotPassword"
    const val registration = "registration"
    const val registrationExtensionData = "registrationExtensionData"
    const val map = "map"
}
