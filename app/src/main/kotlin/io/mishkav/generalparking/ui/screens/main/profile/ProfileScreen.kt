package io.mishkav.generalparking.ui.screens.main.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.ScreenTextfield
import io.mishkav.generalparking.ui.components.lines.TextfieldUnderLine
import io.mishkav.generalparking.ui.theme.Red
import io.mishkav.generalparking.ui.theme.Shapes
import io.mishkav.generalparking.ui.theme.Typography

@Composable
fun ProfileScreen() {

    var textName by rememberSaveable { mutableStateOf("") }
    var textPhone by rememberSaveable { mutableStateOf("") }
    var textEmail by rememberSaveable { mutableStateOf("") }
    var textAuto by rememberSaveable { mutableStateOf("") }
    var textNumAuto by rememberSaveable { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_avatar),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(100.dp)
                .height(90.dp)
                .weight(1.2f)
        )
        TextField(
            value = textName,
            onValueChange = {
                textName = it
            },
            label = { Text(stringResource(R.string.name)) },
            textStyle = TextStyle(fontSize = 40.sp, fontWeight = Bold),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent,
            ),
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterHorizontally)
                .width(IntrinsicSize.Min)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2.2f)
                .padding(vertical = 8.dp)
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
                label = "",
                placeholder = stringResource(R.string.profile_default_phone),
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
                keyboardType = KeyboardType.Email,
                label = "",
                placeholder = stringResource(R.string.profile_default_email),
                modifier = Modifier
                    .fillMaxWidth()
            )
            TextfieldUnderLine()
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2.2f)
                .padding(vertical = 8.dp)
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
                label = "",
                placeholder = stringResource(R.string.profile_default_auto),
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
                keyboardType = KeyboardType.Text,
                label = "",
                placeholder = stringResource(R.string.profile_default_num_auto),
                modifier = Modifier
                    .fillMaxWidth()
            )
            TextfieldUnderLine()
        }
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.profile_cards),
                style = Typography.subtitle1
            )
            TextfieldUnderLine()
        }
        Box(
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            Button(
                onClick = { },
                shape = Shapes.large,
                colors = ButtonDefaults.buttonColors(backgroundColor = Red),
                modifier = Modifier
                    .width(220.dp)
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
    ProfileScreen()
}