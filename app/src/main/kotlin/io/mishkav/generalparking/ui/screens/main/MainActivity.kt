package io.mishkav.generalparking.ui.screens.main

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import io.mishkav.generalparking.state.Session
import io.mishkav.generalparking.ui.screens.auth.authorization.AuthorizationScreen
import io.mishkav.generalparking.ui.screens.auth.confirmEmail.ConfirmEmailScreen
import io.mishkav.generalparking.ui.screens.auth.forgotPassword.ForgotPasswordScreen
import io.mishkav.generalparking.ui.screens.auth.registration.RegistrationScreen
import io.mishkav.generalparking.ui.screens.auth.registrationExtensionData.RegistrationExtensionData
import io.mishkav.generalparking.ui.screens.map.MapScreen
import io.mishkav.generalparking.ui.screens.payment.ChangeMethodScreen
import io.mishkav.generalparking.ui.screens.payment.PaymentScreen
import io.mishkav.generalparking.ui.screens.scheme.SchemeScreen
import io.mishkav.generalparking.ui.screens.profile.ProfileScreen
import io.mishkav.generalparking.ui.theme.GeneralParkingTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val startAgreementActivity = { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Session.AGREEMENT_URI))) }

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
                                paddingValues = it,
                                startAgreementActivity = startAgreementActivity
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
    paddingValues: PaddingValues,
    startAgreementActivity: () -> Unit
) {
    val onError: @Composable (Int) -> Unit = { message ->
        val strMessage = stringResource(message)
        LaunchedEffect(Unit) {
            scaffoldState.snackbarHostState.showSnackbar(strMessage)
        }
    }
    // TODO Migrate from onErro to showMessage
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val showMessage: (Int) -> Unit = { message ->
        val strMessage = context.getString(message)
        scope.launch {
            scaffoldState.snackbarHostState.showSnackbar(strMessage)
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        NavHost(
            navController = navController,
            startDestination = if (viewModel.isAuthorized) Routes.map else Routes.authorization
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
                    onError = onError
                )
            }

            composable(Routes.registration) {
                RegistrationScreen(
                    navController = navController,
                    onError = onError,
                    startAgreementActivity = startAgreementActivity
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
                    navController = navController
                )
            }

            composable(Routes.scheme) {
                SchemeScreen(
                    navController = navController,
                    onError = onError
                )
            }

            composable(Routes.profile) {
                ProfileScreen(
                    navController = navController
                )
            }

            composable(Routes.payment) {
                PaymentScreen(
                    navController = navController,
                    showMessage = showMessage
                )
            }

            composable(Routes.changeMethod) {
                ChangeMethodScreen(
                    navController = navController
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
    const val scheme = "scheme"
    const val profile = "profile"
    const val payment = "payment"
    const val changeMethod = "changeMethod"
}
