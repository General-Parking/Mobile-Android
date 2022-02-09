package io.mishkav.generalparking.ui.screens.main.authorization

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.ScreenTextfield
import io.mishkav.generalparking.ui.components.buttons.LowerButton
import io.mishkav.generalparking.ui.components.buttons.SubRowButtons
import io.mishkav.generalparking.ui.components.lines.TextfieldUnderLine
import io.mishkav.generalparking.ui.components.texts.ScreenBody
import io.mishkav.generalparking.ui.components.texts.ScreenTitle
import io.mishkav.generalparking.ui.theme.Typography
import io.mishkav.generalparking.ui.theme.Yellow

@Composable
fun AuthorizationScreen() {

    var textEmail by rememberSaveable { mutableStateOf("") }
    var textPassword by rememberSaveable { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 40.dp)
    ) {
        ScreenTitle(
            text = stringResource(R.string.authorization),
            modifier = Modifier.weight(2f)
        )
        ScreenBody(
            text = stringResource(R.string.authorization_welcome),
            modifier = Modifier.weight(1f)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                ScreenTextfield(
                    value = textEmail,
                    onValueChange = {
                        textEmail = it
                    },
                    keyboardType = KeyboardType.Email,
                    label = stringResource(R.string.email),
                    placeholder = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                )
                TextfieldUnderLine()
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ScreenTextfield(
                        value = textPassword,
                        onValueChange = {
                            textPassword = it
                        },
                        keyboardType = KeyboardType.Password,
                        label = stringResource(R.string.password),
                        placeholder = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 1.dp)
                    )
                    Text(
                        text = stringResource(R.string.forgot_password),
                        style = Typography.body2,
                        color = Yellow,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(vertical = 15.dp)
                    )
                }
                TextfieldUnderLine()
            }
        }

        LowerButton(
            text = stringResource(R.string.log_in),
            onClick = {},
            modifier = Modifier.weight(2f)
        )
        SubRowButtons(
            text1 = stringResource(R.string.no_account),
            text2 = stringResource(R.string.create),
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
fun PreviewAuthorizationScreen() {
    AuthorizationScreen()
}
