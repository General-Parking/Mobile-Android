package io.mishkav.generalparking.ui.screens.auth.authorization

import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.accompanist.insets.systemBarsPadding
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.buttons.SimpleTextButton
import io.mishkav.generalparking.ui.components.buttons.TextButton
import io.mishkav.generalparking.ui.components.loaders.CircularLoader
import io.mishkav.generalparking.ui.components.textfields.UnderlinedTextfield
import io.mishkav.generalparking.ui.components.texts.ScreenBody
import io.mishkav.generalparking.ui.components.texts.ScreenTitle
import io.mishkav.generalparking.ui.screens.auth.AuthViewModel
import io.mishkav.generalparking.ui.screens.main.Routes
import io.mishkav.generalparking.ui.theme.Gray700
import io.mishkav.generalparking.ui.theme.Shapes
import io.mishkav.generalparking.ui.theme.Typography
import io.mishkav.generalparking.ui.theme.generalParkingLightBackground
import io.mishkav.generalparking.ui.utils.BiometricAuth
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
    val biometricStatus = BiometricAuth.status(LocalContext.current)
    val getSavedParams by viewModel.getSavedParams.collectAsState()

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
        biometricStatus = biometricStatus,
        signIn = viewModel::signIn,
        changeGetSavedParams = viewModel::changeGetSavedParams,
        getSavedParams = getSavedParams,
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
    biometricStatus: Boolean = false,
    signIn: (email: String, password: String, saveParams: Boolean, sharedPreferences: SharedPreferences) -> Unit = { _, _, _, _ -> },
    changeGetSavedParams: (flag: String) -> Unit = { _ -> },
    getSavedParams: String = "false",
    navigateToRegistrationScreen: () -> Unit = {},
    navigateToForgotPasswordScreen: () -> Unit = {}
) {
    var textEmail by rememberSaveable { mutableStateOf("") }
    var textPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    var showAlertSetBiometric by remember { mutableStateOf(false) }

    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    val sharedPreferences = EncryptedSharedPreferences.create(
        "secret_shared_prefs",
        masterKeyAlias,
        LocalContext.current,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    if (getSavedParams == "true") {
        textEmail = sharedPreferences.getString("user.email", "").toString()
        textPassword = sharedPreferences.getString("user.password", "").toString()
        signIn(
            textEmail,
            textPassword,
            false,
            sharedPreferences
        )
        changeGetSavedParams("false")
    }

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
                .weight(2f)
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

            val context = LocalContext.current

            if (biometricStatus) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        onClick = {
                            if (sharedPreferences.getString("user.email", "").toString().isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "You need to sign in",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } else {
                                context.startActivity(Intent(context, BiometricPrompt::class.java))
                            }
                        }
                    ) {
                        Icon(
                            painterResource(R.drawable.ic_biometric),
                            "",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .padding(top = 22.dp)
                                .size(42.dp),
                        )
                    }
                }
            }
        }

        if (showAlertSetBiometric) {
            AlertDialog(
                modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                onDismissRequest = {},
                text = {
                    ScreenBody(
                        text = stringResource(R.string.use_biometric)
                    )
                },
                backgroundColor = MaterialTheme.colorScheme.background,
                buttons = {
                    SubcomposeRow(
                        modifier = Modifier.padding(all = 8.dp).fillMaxWidth(),
                        paddingBetween = 20.dp
                    ) {
                        Button(
                            onClick = {
                                signIn(
                                    textEmail,
                                    textPassword,
                                    true,
                                    sharedPreferences
                                )
                                showAlertSetBiometric = false
                            },
                            shape = Shapes.small,
                            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text(
                                text = stringResource(R.string.yes),
                                color = generalParkingLightBackground,
                                style = Typography.button
                            )
                        }
                        Button(
                            onClick = {
                                signIn(
                                    textEmail,
                                    textPassword,
                                    false,
                                    sharedPreferences
                                )
                                showAlertSetBiometric = false
                            },
                            shape = Shapes.small,
                            colors = ButtonDefaults.buttonColors(backgroundColor = Gray700)
                        ) {
                            Text(
                                text = stringResource(R.string.no),
                                color = generalParkingLightBackground,
                                style = Typography.button
                            )
                        }
                    }
                }
            )
        }

        TextButton(
            text = stringResource(R.string.log_in),
            onClick = {
                if (textEmail != sharedPreferences.getString("user.email", "").toString()
                    && textEmail.isNotEmpty() && biometricStatus
                ) {
                    showAlertSetBiometric = true
                } else {
                    signIn(
                        textEmail,
                        textPassword,
                        false,
                        sharedPreferences
                    )
                }
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

@Composable
private fun SubcomposeRow(
    modifier: Modifier = Modifier,
    paddingBetween: Dp = 0.dp,
    content: @Composable () -> Unit = {},
) {
    val density = LocalDensity.current

    SubcomposeLayout(modifier = modifier) { constraints ->

        var subcomposeIndex = 0

        val spaceBetweenButtons = with(density) {
            paddingBetween.roundToPx()
        }

        var placeables: List<Placeable> = subcompose(subcomposeIndex++, content)
            .map {
                it.measure(constraints)
            }

        var maxWidth = 0
        var maxHeight = 0
        var layoutWidth = 0

        placeables.forEach { placeable: Placeable ->
            maxWidth = placeable.width.coerceAtLeast(maxWidth)
                .coerceAtMost(((constraints.maxWidth - spaceBetweenButtons) / 2))
            maxHeight = placeable.height.coerceAtLeast(maxHeight)
        }


        layoutWidth = maxWidth

        // Remeasure every element using width of longest item using it as min width
        // Our max width is half of the remaining area after we subtract space between buttons
        // and we constraint its maximum width to half width minus space between
        if (placeables.isNotEmpty() && placeables.size > 1) {
            placeables = subcompose(subcomposeIndex, content).map { measurable: Measurable ->
                measurable.measure(
                    constraints.copy(
                        minWidth = maxWidth,
                        maxWidth = ((constraints.maxWidth - spaceBetweenButtons) / 2)
                            .coerceAtLeast(maxWidth)
                    )
                )
            }

            layoutWidth = (placeables.sumOf { it.width } + spaceBetweenButtons)
                .coerceAtMost(constraints.maxWidth)

            maxHeight = placeables.maxOf { it.height }
        }

        layout(layoutWidth, maxHeight) {
            var xPos = 0
            placeables.forEach { placeable: Placeable ->
                placeable.placeRelative(xPos, 0)
                xPos += placeable.width + spaceBetweenButtons
            }
        }
    }
}