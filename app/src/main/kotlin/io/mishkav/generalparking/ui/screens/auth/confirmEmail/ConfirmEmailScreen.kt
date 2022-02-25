package io.mishkav.generalparking.ui.screens.auth.confirmEmail

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.buttons.TextButton
import io.mishkav.generalparking.ui.components.buttons.CreateButton
import io.mishkav.generalparking.ui.components.texts.ScreenBody
import io.mishkav.generalparking.ui.components.texts.ScreenTitle
import io.mishkav.generalparking.ui.screens.auth.AuthViewModel
import io.mishkav.generalparking.ui.screens.main.Routes
import io.mishkav.generalparking.ui.theme.Typography
import io.mishkav.generalparking.ui.utils.ErrorResult
import io.mishkav.generalparking.ui.utils.LoadingResult
import io.mishkav.generalparking.ui.utils.SuccessResult
import kotlinx.coroutines.Job
import kotlin.reflect.KFunction1

@Composable
fun ConfirmEmailScreen(
    navController: NavHostController,
    onError: @Composable (Int) -> Unit
) {
    val viewModel: AuthViewModel = viewModel()
    val isEmailVerified by viewModel.isEmailVerified.collectAsState()
    val navigateToAuthorization: () -> Unit = {
        navController.navigate(Routes.authorization)
    }

    isEmailVerified.data?.let { result ->
        if (result) {
            LaunchedEffect(Unit) {
                navController.navigate(Routes.registrationExtensionData)
            }
        } else
            onError(R.string.error_email_not_verified)

    }

    ConfirmEmailScreenContent(
        checkIsEmailVerified = viewModel::checkIsEmailVerified,
        navigateToAuthorization = navigateToAuthorization
    )
}

@Composable
fun ConfirmEmailScreenContent(
    checkIsEmailVerified: () -> Unit = {},
    navigateToAuthorization: () -> Unit = {}
) {
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
            text = stringResource(R.string.confirm_email),
            modifier = Modifier.weight(1f)
        )
        ScreenBody(
            text = stringResource(R.string.confirm_email_text),
            modifier = Modifier.weight(1f)
        )

        TextButton(
            text = stringResource(R.string.confirm_continue),
            onClick = checkIsEmailVerified,
            modifier = Modifier.weight(3f)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)

        ) {
            Text(
                text = stringResource(R.string.other_account),
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
fun PreviewConfirmEmailScreen() {
    ConfirmEmailScreenContent()
}