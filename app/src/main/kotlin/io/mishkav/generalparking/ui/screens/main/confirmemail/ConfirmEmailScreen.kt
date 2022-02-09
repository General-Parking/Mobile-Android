package io.mishkav.generalparking.ui.screens.main.confirmemail

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.buttons.LowerButton
import io.mishkav.generalparking.ui.components.buttons.SubRowButtons
import io.mishkav.generalparking.ui.components.texts.ScreenBody
import io.mishkav.generalparking.ui.components.texts.ScreenTitle

@Composable
fun ConfirmEmailScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 40.dp)
    ) {
        ScreenTitle(
            text = stringResource(R.string.confirm_email),
            modifier = Modifier.weight(1f)
        )
        ScreenBody(
            text = stringResource(R.string.confirm_email_text),
            modifier = Modifier.weight(1f)
        )

        LowerButton(
            text = stringResource(R.string.confirm_continue),
            onClick = {},
            modifier = Modifier.weight(3f)
        )

        SubRowButtons(
            text1 = stringResource(R.string.other_account),
            text2 = stringResource(R.string.log_in),
            onClick1 = { },
            onClick2 = { },
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 30.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConfirmEmailScreen() {
    ConfirmEmailScreen()
}