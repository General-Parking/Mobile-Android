package io.mishkav.generalparking.ui.screens.auth.registrationExtensionData

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.UnderlinedTextfield
import io.mishkav.generalparking.ui.components.buttons.TextButton
import io.mishkav.generalparking.ui.components.loaders.CircularLoader
import io.mishkav.generalparking.ui.components.texts.ScreenBody
import io.mishkav.generalparking.ui.components.texts.ScreenTitle
import io.mishkav.generalparking.ui.screens.auth.AuthViewModel
import io.mishkav.generalparking.ui.screens.main.Routes
import io.mishkav.generalparking.ui.theme.GeneralParkingTheme
import io.mishkav.generalparking.ui.theme.Shapes
import io.mishkav.generalparking.ui.utils.ErrorResult
import io.mishkav.generalparking.ui.utils.LoadingResult
import io.mishkav.generalparking.ui.utils.SuccessResult

@Composable
fun RegistrationExtensionData(
    navController: NavHostController,
    onError: @Composable (Int) -> Unit
) {
    val viewModel: AuthViewModel = viewModel()
    val currentUser by viewModel.currentUser.collectAsState()
    currentUser.also { result ->
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

    RegistrationExtensionDataContent(
        insertExtensionUserData = viewModel::insertExtensionUserData
    )
}

fun String.onlyLetters() = all { it.isLetter() }

@Composable
fun RegistrationExtensionDataContent(
    insertExtensionUserData: (numberAuto: String, carBrand: String, phoneNumber: String) -> Unit = { _, _, _ -> }
) {

    var textNumberAutoLeftSymbols by rememberSaveable { mutableStateOf("") }
    var textNumberAutoDigits by rememberSaveable { mutableStateOf("") }
    var textNumberAutoRightSymbols by rememberSaveable { mutableStateOf("") }
    var textNumberAutoRegion by rememberSaveable { mutableStateOf("") }
    var textModel by rememberSaveable { mutableStateOf("") }
    var textPhone by rememberSaveable { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val colors = TextFieldDefaults.textFieldColors(
        textColor = MaterialTheme.colorScheme.onPrimary,
        cursorColor = MaterialTheme.colorScheme.primary,
        disabledLabelColor = MaterialTheme.colorScheme.primary,
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = MaterialTheme.colorScheme.primary,
        placeholderColor = MaterialTheme.colorScheme.onPrimary,
        focusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        backgroundColor = Color.Transparent,
    )

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
            modifier = Modifier.weight(1f)
        )
        ScreenBody(
            text = stringResource(R.string.input_data_text),
            modifier = Modifier.weight(1f)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(303.dp)
                    .height(103.dp)
                    .background(
                        Color.White,
                        shape = Shapes.small
                    )
                    .align(Alignment.CenterHorizontally)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .width(300.dp)
                        .height(100.dp)
                        .background(
                            Color.Black,
                            shape = Shapes.small
                        )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .width(200.dp)
                            .fillMaxHeight()
                            .background(
                                Color.White,
                                shape = Shapes.small
                            )
                    ) {

                        TextField(
                            value = textNumberAutoLeftSymbols,
                            onValueChange = {
                                if (it.length <= 1)
                                    textNumberAutoLeftSymbols =
                                        if (it == it.uppercase() && it.onlyLetters()) it
                                        else ""
                                if (textNumberAutoLeftSymbols.length == 1)
                                    focusManager.moveFocus(FocusDirection.Right)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                capitalization = KeyboardCapitalization.Characters
                            ),
                            placeholder = { Text(stringResource(R.string.a)) },
                            textStyle = TextStyle(fontSize = 23.sp, fontWeight = FontWeight.Bold),
                            colors = colors,
                            modifier = Modifier
                                .width(50.dp)
                                .fillMaxHeight()
                        )
                        TextField(
                            value = textNumberAutoDigits,
                            onValueChange = {
                                if (it.length <= 3)
                                    textNumberAutoDigits =
                                        if (it.isDigitsOnly()) it
                                        else ""
                                if (textNumberAutoDigits.length == 3)
                                    focusManager.moveFocus(FocusDirection.Right)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            placeholder = { Text(stringResource(R.string.zeros)) },
                            textStyle = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold),
                            colors = colors,
                            modifier = Modifier
                                .width(80.dp)
                                .fillMaxHeight()
                        )
                        TextField(
                            value = textNumberAutoRightSymbols,
                            onValueChange = {
                                if (it.length <= 2)
                                    textNumberAutoRightSymbols =
                                        if (it == it.uppercase() && it.onlyLetters()) it
                                        else ""
                                if (textNumberAutoRightSymbols.length == 2)
                                    focusManager.moveFocus(FocusDirection.Right)
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                capitalization = KeyboardCapitalization.Characters
                            ),
                            placeholder = { Text(stringResource(R.string.aa)) },
                            textStyle = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                            colors = colors,
                            modifier = Modifier
                                .width(70.dp)
                                .fillMaxHeight()
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(vertical = 3.dp)
                            .width(90.dp)
                            .fillMaxHeight()
                            .background(
                                Color.White,
                                shape = Shapes.small
                            )
                    ) {
                        TextField(
                            value = textNumberAutoRegion,
                            onValueChange = {
                                if (it.length <= 3)
                                    textNumberAutoRegion =
                                        if (it.isDigitsOnly()) it
                                        else ""
                                if (textNumberAutoRegion.length == 3)
                                    focusManager.moveFocus(FocusDirection.Down)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                            placeholder = { Text(stringResource(R.string.zeros)) },
                            textStyle = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold),
                            colors = colors,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                        )
                        Text(
                            text = stringResource(R.string.rus),
                            fontSize = 20.sp,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f)
        ) {
            UnderlinedTextfield(
                value = textModel,
                onValueChange = {
                    textModel = it
                },
                keyboardType = KeyboardType.Text,
                label = {
                    Text(
                        text = stringResource(R.string.car_model),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            )
            UnderlinedTextfield(
                value = textPhone,
                onValueChange = {
                    textPhone = it
                },
                keyboardType = KeyboardType.Phone,
                label = {
                    Text(
                        text = stringResource(R.string.phone),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            )

        }

        TextButton(
            text = stringResource(R.string.create_account),
            onClick = {
                insertExtensionUserData(
                    textNumberAutoLeftSymbols + textNumberAutoDigits + textNumberAutoRightSymbols + textNumberAutoRegion,
                    textModel,
                    textPhone
                )
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInputDataScreen() {
    GeneralParkingTheme {
        RegistrationExtensionDataContent()
    }
}
