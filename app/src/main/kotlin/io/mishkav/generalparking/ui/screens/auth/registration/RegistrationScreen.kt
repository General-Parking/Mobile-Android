package io.mishkav.generalparking.ui.screens.auth.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.textfields.UnderlinedTextfield
import io.mishkav.generalparking.ui.components.buttons.TextButton
import io.mishkav.generalparking.ui.components.buttons.SimpleTextButton
import io.mishkav.generalparking.ui.components.loaders.CircularLoader
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
    onError: @Composable (Int) -> Unit,
    startAgreementActivity: () -> Unit
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

    RegistrationScreenContent(
        createNewUser = viewModel::createNewUser,
        navigateToAuthorization = navigateToAuthorization,
        startAgreementActivity = startAgreementActivity
    )
}

@Composable
fun RegistrationScreenContent(
    createNewUser: (name: String, email: String, password: String) -> Unit = { _, _, _ -> },
    navigateToAuthorization: () -> Unit = {},
    startAgreementActivity: () -> Unit = {}
) {

    var textEmail by rememberSaveable { mutableStateOf("") }
    var textPassword by rememberSaveable { mutableStateOf("") }
    var textName by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
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
            UnderlinedTextfield(
                value = textName,
                onValueChange = {
                    textName = it
                },
                keyboardType = KeyboardType.Text,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                label = {
                    Text(
                        text = stringResource(R.string.name),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            )
            UnderlinedTextfield(
                value = textEmail,
                onValueChange = {
                    textEmail = it
                },
                keyboardType = KeyboardType.Email,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
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
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                label = { Text(stringResource(R.string.password)) },
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
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            ClickableText(
                text = AnnotatedString(
                    text = stringResource(R.string.use_terms_href),
                    spanStyle = SpanStyle(
                        color = Color.Blue,
                        fontWeight = FontWeight.Bold
                    )
                ),
                onClick = { startAgreementActivity() },
                style = Typography.caption,
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
                color = MaterialTheme.colorScheme.onPrimary,
                style = Typography.subtitle1
            )
            SimpleTextButton(
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