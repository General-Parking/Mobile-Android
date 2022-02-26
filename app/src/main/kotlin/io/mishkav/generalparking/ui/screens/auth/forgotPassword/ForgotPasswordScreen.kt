package io.mishkav.generalparking.ui.screens.auth.forgotPassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.ScreenTextfield
import io.mishkav.generalparking.ui.components.buttons.TextButton
import io.mishkav.generalparking.ui.components.lines.TextfieldUnderLine
import io.mishkav.generalparking.ui.components.texts.ScreenBody
import io.mishkav.generalparking.ui.components.texts.ScreenTitle
import io.mishkav.generalparking.ui.screens.auth.AuthViewModel
import io.mishkav.generalparking.ui.utils.ErrorResult
import io.mishkav.generalparking.ui.utils.LoadingResult
import io.mishkav.generalparking.ui.utils.SuccessResult

@Composable
fun ForgotPasswordScreen(
    navController: NavHostController,
    onShowMessage: @Composable (Int) -> Unit
) {
    val viewModel: AuthViewModel = viewModel()
    val resetPasswordResult by viewModel.resetPasswordResult.collectAsState()
    resetPasswordResult.also { result ->
        when (result) {
            is ErrorResult -> onShowMessage(result.message!!)
            is SuccessResult -> {
                onShowMessage(R.string.success_sending_email)
            }
            is LoadingResult -> {}
        }
    }

    ForgotPasswordScreenContent(
        resetPassword = viewModel::resetPassword
    )
}

@Composable
fun ForgotPasswordScreenContent(
    resetPassword: (email: String) -> Unit = { _ -> }
) {

    var textEmail by rememberSaveable { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.main_hor_padding), vertical = 60.dp)
    ) {
        ScreenTitle(
            text = stringResource(R.string.forgot_password),
            modifier = Modifier.weight(1f)
        )
        ScreenBody(
            text = stringResource(R.string.forgot_password_text),
            modifier = Modifier.weight(1f)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            ScreenTextfield(
                value = textEmail,
                onValueChange = {
                    textEmail = it
                },
                keyboardType = KeyboardType.Email,
                label = { Text(stringResource(R.string.email)) },
                modifier = Modifier
                    .fillMaxWidth()
            )
            TextfieldUnderLine()
        }

        TextButton(
            text = stringResource(R.string.continue_text),
            onClick = {
                resetPassword(
                    textEmail
                )
            },
            modifier = Modifier.weight(5f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewForgotPasswordScreen() {
    ForgotPasswordScreenContent()
}