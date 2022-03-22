package io.mishkav.generalparking.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.mishkav.generalparking.R
import io.mishkav.generalparking.data.utils.UserFields
import io.mishkav.generalparking.ui.components.loaders.CircularLoader
import io.mishkav.generalparking.ui.components.topAppBar.TopAppBarWithBackButton
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
                    name = currentUser.data?.metaUserInfo?.get(UserFields.FIELD_NAME)
                        ?: stringResource(R.string.username),
                    phone = currentUser.data?.metaUserInfo?.get(UserFields.FIELD_PHONE_NUMBER)
                        ?: stringResource(R.string.profile_default_phone),
                    email = currentUser.data?.email ?: stringResource(R.string.profile_email),
                    auto = currentUser.data?.metaUserInfo?.get(UserFields.FIELD_CAR_BRAND)
                        ?: stringResource(R.string.profile_default_auto),
                    numAuto = currentUser.data?.numberAuto ?: stringResource(R.string.profile_default_num_auto),
                    signOut = viewModel::signOut,
                    navigateBack = navController::popBackStack
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
    name: String,
    phone: String,
    email: String,
    auto: String,
    numAuto: String,
    signOut: () -> Unit = { },
    navigateBack: () -> Unit = { }
) = Column(
    modifier = Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    var textName by rememberSaveable { mutableStateOf(name) }
    var textPhone by rememberSaveable { mutableStateOf(phone) }
    var textEmail by rememberSaveable { mutableStateOf(email) }
    var textAuto by rememberSaveable { mutableStateOf(auto) }
    var textNumAuto by rememberSaveable { mutableStateOf(numAuto) }


    TopAppBarWithBackButton(
        title = {
            Text(
                text = stringResource(R.string.profile),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigateBack = navigateBack
    )

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        item {

            Spacer(Modifier.height(16.dp))

            Image(
                painter = painterResource(R.drawable.ic_avatar),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = textName,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(Modifier.height(32.dp))

            OutlinedField(
                text = textPhone,
                onValueChange = { textPhone = it }
            ) {
                Text(
                    text = stringResource(R.string.phone),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(Modifier.height(8.dp))

            OutlinedField(
                text = textEmail,
                onValueChange = { textEmail = it }
            ) {
                Text(
                    text = stringResource(R.string.email),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(Modifier.height(8.dp))

            OutlinedField(
                text = textAuto,
                onValueChange = { textAuto = it }
            ) {
                Text(
                    text = stringResource(R.string.profile_auto),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(Modifier.height(8.dp))

            OutlinedField(
                text = textNumAuto,
                onValueChange = { textNumAuto = it }
            ) {
                Text(
                    text = stringResource(R.string.profile_num_auto),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = signOut,
                shape = Shapes.large,
                colors = ButtonDefaults.buttonColors(backgroundColor = Red800),
                modifier = Modifier
                    .width(dimensionResource(R.dimen.exit_button_width))
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

@Composable
private fun OutlinedField(
    text: String,
    isReadOnly: Boolean = true,
    onValueChange: (str: String) -> Unit = {},
    label: @Composable () -> Unit = {}
) = OutlinedTextField(
    value = text,
    textStyle = MaterialTheme.typography.bodyLarge,
    readOnly = isReadOnly,
    colors = TextFieldDefaults.textFieldColors(
        textColor = MaterialTheme.colorScheme.onPrimary,
        cursorColor = MaterialTheme.colorScheme.primary,
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = MaterialTheme.colorScheme.primary,
        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.primary
    ),
    onValueChange = onValueChange,
    singleLine = true,
    maxLines = 1,
    label = label,
    shape = Shapes.medium,
    modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .padding(
            horizontal = dimensionResource(R.dimen.main_hor_padding),
            vertical = 4.dp
        )
)

@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    GeneralParkingTheme {
        ProfileScreenContent(
            signOut = {},
            name = stringResource(R.string.username),
            phone = stringResource(R.string.profile_default_phone),
            email = stringResource(R.string.profile_email),
            auto = stringResource(R.string.profile_default_auto),
            numAuto = stringResource(R.string.profile_default_num_auto)
        )
    }
}