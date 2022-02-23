package io.mishkav.generalparking.ui.screens.auth.authorization

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import io.mishkav.generalparking.ui.components.buttons.CreateButton
import io.mishkav.generalparking.ui.components.lines.TextfieldUnderLine
import io.mishkav.generalparking.ui.components.texts.ScreenBody
import io.mishkav.generalparking.ui.components.texts.ScreenTitle
import io.mishkav.generalparking.ui.screens.auth.AuthViewModel
import io.mishkav.generalparking.ui.screens.main.Routes
import io.mishkav.generalparking.ui.theme.Typography
import io.mishkav.generalparking.ui.utils.ErrorResult
import io.mishkav.generalparking.ui.utils.LoadingResult
import io.mishkav.generalparking.ui.utils.SuccessResult

@Composable
fun AuthorizationScreen(
    navController: NavHostController
) {
    val viewModel: AuthViewModel = viewModel()
    val signInResult by viewModel.signInResult.collectAsState()
    signInResult.also { result ->
        when (result) {
            is ErrorResult -> {}
            is SuccessResult -> {
                //На карты
                //navController.navigate()
            }
            is LoadingResult -> {}
        }
    }

    AuthorizationScreenContent(
        signIn = viewModel::signIn,
        navigateToRegistrationScreen = {
            navController.navigate(Routes.registration)
        }
    )
}

@Composable
fun AuthorizationScreenContent(
    signIn: (email: String, password: String) -> Unit = { _, _ -> },
    navigateToRegistrationScreen: () -> Unit = {}
) {

    var textEmail by rememberSaveable { mutableStateOf("") }
    var textPassword by rememberSaveable { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ScreenTextfield(
                        value = textPassword,
                        onValueChange = {
                            textPassword = it
                        },
                        keyboardType = KeyboardType.Password,
                        label = { Text(stringResource(R.string.password)) },
                        modifier = Modifier.width(250.dp)
                    )
                    // CreateButton(
                    //     text = stringResource(R.string.forgot_password),
                    //     onClick = { },
                    //     modifier = Modifier.width(150.dp)
                    // )
                }
                TextfieldUnderLine()
            }
        }

        TextButton(
            text = stringResource(R.string.log_in),
            onClick = {},
            modifier = Modifier.weight(1f)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)

        ) {
            Text(
                text = stringResource(R.string.no_account),
                color = Color.Black,
                style = Typography.subtitle1
            )
            CreateButton(
                text = stringResource(R.string.create),
                onClick = navigateToRegistrationScreen
            )
        }

        CreateButton(
            text = stringResource(R.string.forgot_password),
            onClick = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAuthorizationScreen() {
    AuthorizationScreenContent()
}
