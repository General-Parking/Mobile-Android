package io.mishkav.generalparking.ui.screens.auth.registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
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
fun RegistrationScreen(
    navController: NavHostController,
    onError: @Composable (Int) -> Unit
) {
    val viewModel: AuthViewModel = viewModel()
    val createNewUserResult by viewModel.createNewUserResult.collectAsState()
    val navigateToAuthorization: () -> Unit = {
        navController.navigate(Routes.authorization)
    }

    createNewUserResult.also { result ->
        when (result) {
            is ErrorResult -> onError(result.message!!)
            is SuccessResult -> {
                LaunchedEffect(Unit) {
                    navController.navigate(Routes.confirmEmail)
                }
            }
            is LoadingResult -> {}
        }
    }

    RegistrationScreenContent(
        createNewUser = viewModel::createNewUser,
        navigateToAuthorization = navigateToAuthorization
    )
}

@Composable
fun RegistrationScreenContent(
    createNewUser: (name: String, email: String, password: String) -> Unit = { _, _, _ -> },
    navigateToAuthorization: () -> Unit = {}
) {

    var textEmail by rememberSaveable { mutableStateOf("") }
    var textPassword by rememberSaveable { mutableStateOf("") }
    var textName by rememberSaveable { mutableStateOf("") }

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
            text = stringResource(R.string.registration),
            modifier = Modifier.weight(2f)
        )
        ScreenBody(
            text = stringResource(R.string.registration_text),
            modifier = Modifier.weight(1f)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                ScreenTextfield(
                    value = textName,
                    onValueChange = {
                        textName = it
                    },
                    keyboardType = KeyboardType.Text,
                    label = { Text(stringResource(R.string.name)) },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                TextfieldUnderLine()
            }
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
                ScreenTextfield(
                    value = textPassword,
                    onValueChange = {
                        textPassword = it
                    },
                    keyboardType = KeyboardType.Password,
                    label = { Text(stringResource(R.string.password)) },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                TextfieldUnderLine()
            }
        }

        TextButton(
            text = stringResource(R.string.continue_text),
            onClick = {
                createNewUser(
                    textName,
                    textEmail,
                    textPassword
                )
            },
            modifier = Modifier.weight(1f)
        )

        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = stringResource(R.string.use_terms),
                style = Typography.caption,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = stringResource(R.string.use_terms_href),
                style = Typography.caption,
                color = Color.Blue,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)

        ) {
            Text(
                text = stringResource(R.string.have_account),
                style = Typography.subtitle1
            )
            CreateButton(
                text = stringResource(R.string.log_in),
                onClick = navigateToAuthorization
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegistrationScreen() {
    RegistrationScreenContent()
}