package io.mishkav.generalparking.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    onclick: () -> Unit,
    message: Int,
    navController: NavHostController,
    letPopBack: Boolean = true
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error_animation))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )

    if (letPopBack) {
        TopAppBarWithBackButton(
            title = {
                Text(
                    text = stringResource(R.string.profile),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            navigateBack = navController::popBackStack
        )
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
                text = stringResource(R.string.onerror_body)
            )
            ScreenBody(
                text = stringResource(message),
                modifier = Modifier.padding(top = 10.dp)
            )
            TextButton(
                text = stringResource(R.string.repeat_request),
                onClick = {
                    onclick()
                },
                modifier = Modifier
                    .width(200.dp)
                    .padding(top = 40.dp)
            )
        }
    }
}
