package io.mishkav.generalparking.ui.screens.auth.confirmEmail

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.buttons.TextButton
import io.mishkav.generalparking.ui.components.buttons.SimpleTextButton
import io.mishkav.generalparking.ui.components.texts.ScreenBody
import io.mishkav.generalparking.ui.components.texts.ScreenTitle
import io.mishkav.generalparking.ui.theme.Typography

@Composable
fun ConfirmEmailScreen(
    navController: NavHostController,
    onError: @Composable (Int) -> Unit
) {
    ConfirmEmailScreenContent()
}

@Composable
fun ConfirmEmailScreenContent() {
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
            onClick = {},
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
            SimpleTextButton(
                text = stringResource(R.string.log_in),
                onClick = { }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConfirmEmailScreen() {
    ConfirmEmailScreenContent()
}