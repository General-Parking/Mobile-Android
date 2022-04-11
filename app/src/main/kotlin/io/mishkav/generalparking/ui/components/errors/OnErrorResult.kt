package io.mishkav.generalparking.ui.components.errors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.*
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.buttons.TextButton
import io.mishkav.generalparking.ui.components.texts.ScreenBody
import io.mishkav.generalparking.ui.components.texts.ScreenTitle
import io.mishkav.generalparking.ui.components.topAppBar.TopAppBarWithBackButton

@Composable
fun OnErrorResult(
    onClick: () -> Unit,
    message: Int,
    navController: NavHostController,
    isTopAppBarAvailable: Boolean = true,
    appBarId: Int = R.string.profile
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error_animation))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isTopAppBarAvailable) {
            TopAppBarWithBackButton(
                title = {
                    Text(
                        text = stringResource(appBarId),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigateBack = navController::popBackStack
            )
        }
        LottieAnimation(
            composition,
            progress,
            modifier = Modifier.weight(1f)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            ScreenTitle(
                text = stringResource(R.string.on_error_body),
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.on_error_padding))
            )

            Spacer(Modifier.height(10.dp))

            ScreenBody(
                text = stringResource(message),
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.on_error_padding))
            )

            Spacer(Modifier.height(40.dp))

            TextButton(
                text = stringResource(R.string.repeat_request),
                onClick = {
                    onClick()
                },
                modifier = Modifier
                    .width(dimensionResource(R.dimen.on_error_button_width))
            )
        }
    }
}

