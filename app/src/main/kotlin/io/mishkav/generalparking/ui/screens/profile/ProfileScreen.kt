package io.mishkav.generalparking.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
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
                    keyboardType = KeyboardType.Text,
                    placeholder = { Text(stringResource(R.string.profile_default_num_auto)) },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                TextfieldUnderLine()
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.profile_field_padding))
            ) {
                Text(
                    text = stringResource(R.string.profile_cards),
                    style = Typography.subtitle1
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
                onClick = { },
                shape = Shapes.large,
                colors = ButtonDefaults.buttonColors(backgroundColor = Red),
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
    ProfileScreen()
}