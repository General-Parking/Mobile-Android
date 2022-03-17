package io.mishkav.generalparking.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.mishkav.generalparking.R
import io.mishkav.generalparking.domain.entities.User
import io.mishkav.generalparking.ui.components.ScreenTextfield
import io.mishkav.generalparking.ui.components.lines.TextfieldUnderLine
import io.mishkav.generalparking.ui.components.loaders.CircularLoader
import io.mishkav.generalparking.ui.screens.main.Routes
import io.mishkav.generalparking.ui.theme.GeneralParkingTheme
import io.mishkav.generalparking.ui.theme.Red800
import io.mishkav.generalparking.ui.theme.Shapes
import io.mishkav.generalparking.ui.theme.Typography
import io.mishkav.generalparking.ui.utils.ErrorResult
import io.mishkav.generalparking.ui.utils.LoadingResult
import io.mishkav.generalparking.ui.utils.SuccessResult

@Composable
fun ProfileScreen(
    navController: NavHostController,
    onError: @Composable (Int) -> Unit
) {
    val viewModel: ProfileViewModel = viewModel()
    val signOutResult by viewModel.signOutResult.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getUserDataFromDatabase()
    }

    signOutResult.also { result ->
        when (result) {
            is ErrorResult -> onError(result.message!!)
            is SuccessResult -> {
                LaunchedEffect(Unit) {
                    navController.navigate(Routes.authorization)
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

    currentUser.also { result ->
        when (result) {
            is ErrorResult -> onError(result.message!!)
            is SuccessResult -> {
                ProfileScreenContent(
                    signOut = viewModel::signOut,
                    currentUser = currentUser.data!!
                )
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
}

@Composable
fun ProfileScreenContent(
    signOut: () -> Unit = { },
    currentUser: User
) {
    var textName by rememberSaveable { mutableStateOf(currentUser.metaUserInfo["name"]!!) }
    var textPhone by rememberSaveable { mutableStateOf(currentUser.metaUserInfo["phone_number"]!!) }
    var textEmail by rememberSaveable { mutableStateOf(currentUser.email) }
    var textAuto by rememberSaveable { mutableStateOf(currentUser.metaUserInfo["car_brand"]!!) }
    var textNumAuto by rememberSaveable { mutableStateOf(currentUser.numberAuto) }

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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(7f)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(R.drawable.ic_avatar),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(
                        width = dimensionResource(R.dimen.image_width),
                        height = dimensionResource(R.dimen.image_width)
                    )
            )
            TextField(
                value = textName,
                onValueChange = {
                    textName = it
                },
                label = { Text(stringResource(R.string.name)) },
                textStyle = Typography.h4,
                colors = TextFieldDefaults.textFieldColors(
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
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(IntrinsicSize.Min)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.profile_field_padding))
            ) {
                Text(
                    text = stringResource(R.string.profile_phone),
                    style = Typography.subtitle1
                )
                ScreenTextfield(
                    value = textPhone,
                    onValueChange = {
                        textPhone = it
                    },
                    keyboardType = KeyboardType.Phone,
                    placeholder = { Text(stringResource(R.string.profile_default_phone)) },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Text(
                    text = stringResource(R.string.profile_email),
                    style = Typography.subtitle1
                )
                ScreenTextfield(
                    value = textEmail,
                    onValueChange = {
                        textEmail = it
                    },
                    enabled = false,
                    keyboardType = KeyboardType.Email,
                    placeholder = { Text(stringResource(R.string.profile_default_email)) },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                TextfieldUnderLine()
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.profile_field_padding))
            ) {
                Text(
                    text = stringResource(R.string.profile_auto),
                    style = Typography.subtitle1
                )
                ScreenTextfield(
                    value = textAuto,
                    onValueChange = {
                        textAuto = it
                    },
                    keyboardType = KeyboardType.Text,
                    placeholder = { Text(stringResource(R.string.profile_default_auto)) },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Text(
                    text = stringResource(R.string.profile_num_auto),
                    style = Typography.subtitle1
                )
                ScreenTextfield(
                    value = textNumAuto,
                    onValueChange = {
                        textNumAuto = it
                    },
                    enabled = false,
                    keyboardType = KeyboardType.Text,
                    placeholder = { Text(stringResource(R.string.profile_default_num_auto)) },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                TextfieldUnderLine()
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Button(
                onClick = signOut,
                shape = Shapes.large,
                colors = ButtonDefaults.buttonColors(backgroundColor = Red800),
                modifier = Modifier
                    .width(dimensionResource(R.dimen.exit_button_width))
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = stringResource(R.string.exit),
                    color = Color.White,
                    style = Typography.button
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    GeneralParkingTheme {
        //ProfileScreenContent()
    }
}