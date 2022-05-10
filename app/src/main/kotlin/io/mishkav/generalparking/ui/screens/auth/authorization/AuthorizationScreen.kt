package io.mishkav.generalparking.ui.screens.auth.authorization

import androidx.compose.foundation.background
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.insets.systemBarsPadding
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.textfields.UnderlinedTextfield
import io.mishkav.generalparking.ui.components.buttons.TextButton
import io.mishkav.generalparking.ui.components.buttons.SimpleTextButton
import io.mishkav.generalparking.ui.components.loaders.CircularLoader
import io.mishkav.generalparking.ui.components.texts.ScreenBody
import io.mishkav.generalparking.ui.components.texts.ScreenTitle
import io.mishkav.generalparking.ui.screens.auth.AuthViewModel
import io.mishkav.generalparking.ui.screens.main.Routes
import io.mishkav.generalparking.ui.utils.ErrorResult
import io.mishkav.generalparking.ui.utils.LoadingResult
import io.mishkav.generalparking.ui.utils.SuccessResult

@Composable
fun AuthorizationScreen(
    navController: NavHostController,
    onError: @Composable (Int) -> Unit
) {
    val viewModel: AuthViewModel = viewModel()
    val signInResult by viewModel.signInResult.collectAsState()
    signInResult.also { result ->
        when (result) {
            is ErrorResult -> onError(result.message!!)
            is SuccessResult -> {
                LaunchedEffect(Unit) {
                    navController.navigate(Routes.map)
                }
            }
            is LoadingResult -> {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularLoader()
                }
            }
        }
    }

    AuthorizationScreenContent(
        signIn = viewModel::signIn,
        navigateToRegistrationScreen = {
            navController.navigate(Routes.registration)
        },
        navigateToForgotPasswordScreen = {
            navController.navigate(Routes.forgotPassword)
        }
    )
}

@Composable
fun AuthorizationScreenContent(
    signIn: (email: String, password: String) -> Unit = { _, _ -> },
    navigateToRegistrationScreen: () -> Unit = {},
    navigateToForgotPasswordScreen: () -> Unit = {}
) {

    var textEmail by rememberSaveable { mutableStateOf("") }
    var textPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding()
            .padding(
                horizontal = dimensionResource(R.dimen.main_hor_padding),
                vertical = dimensionResource(R.dimen.main_vert_padding)
            )
    ) {
        ScreenTitle(
            text = stringResource(R.string.authorization),
            modifier = Modifier.weight(1f)
        )

        ScreenBody(
            text = stringResource(R.string.authorization_welcome),
            modifier = Modifier.weight(1f)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f)
        ) {
            UnderlinedTextfield(
                value = textEmail,
                onValueChange = {
                    textEmail = it
                },
                keyboardType = KeyboardType.Email,
                label = {
                    Text(
                        text = stringResource(R.string.email),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            )
            UnderlinedTextfield(
                value = textPassword,
                onValueChange = {
                    textPassword = it
                },
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardType = KeyboardType.Password,
                label = {
                    Text(
                        text = stringResource(R.string.password),
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    val image = if (passwordVisibility)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff

                    IconButton(onClick = {
                        passwordVisibility = !passwordVisibility
                    }) {
                        Icon(
                            imageVector = image, "",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }

        TextButton(
            text = stringResource(R.string.log_in),
            onClick = {
                signIn(
                    textEmail,
                    textPassword
                )
            },
            modifier = Modifier.weight(1f)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)

        ) {
            SimpleTextButton(
                text = stringResource(R.string.forgot_password),
                onClick = navigateToForgotPasswordScreen
            )

            SimpleTextButton(
                text = stringResource(R.string.create),
                onClick = navigateToRegistrationScreen
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAuthorizationScreen() {
    AuthorizationScreenContent()
}
