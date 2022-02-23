package io.mishkav.generalparking.ui.screens.auth.registrationExtensionData

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.mishkav.generalparking.ui.theme.Typography
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.ScreenTextfield
import io.mishkav.generalparking.ui.components.buttons.TextButton
import io.mishkav.generalparking.ui.components.lines.TextfieldUnderLine
import io.mishkav.generalparking.ui.components.texts.ScreenBody
import io.mishkav.generalparking.ui.components.texts.ScreenTitle
import io.mishkav.generalparking.ui.theme.Shapes

@Composable
fun RegistrationExtensionData(navController: NavHostController) {
    RegistrationExtensionDataContent()
}

@Composable
fun RegistrationExtensionDataContent() {

    var textRightZeros by rememberSaveable { mutableStateOf("") }
    var textLeftSymbol by rememberSaveable { mutableStateOf("") }
    var textLeftZeros by rememberSaveable { mutableStateOf("") }
    var textRightSymbol by rememberSaveable { mutableStateOf("") }
    var textModel by rememberSaveable { mutableStateOf("") }
    var textPhone by rememberSaveable { mutableStateOf("") }
    var textCard by rememberSaveable { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.main_hor_padding), vertical = dimensionResource(R.dimen.main_vert_padding))
    ) {
        ScreenTitle(
            text = stringResource(R.string.registration),
            modifier = Modifier.weight(1f)
        )
        ScreenBody(
            text = stringResource(R.string.input_data_text),
            modifier = Modifier.weight(1f)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(300.dp)
                    .height(90.dp)
                    .background(
                        Color.Black,
                        shape = Shapes.small
                    )
                    .align(Alignment.CenterHorizontally)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .width(200.dp)
                        .fillMaxHeight()
                        .background(
                            Color.White,
                            shape = Shapes.small
                        )
                ) {

                    TextField(
                        value = textLeftSymbol,
                        onValueChange = {
                            if (it.length <= 1)
                                textLeftSymbol = it
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        placeholder = { Text(stringResource(R.string.a)) },
                        textStyle = TextStyle(fontSize = 23.sp, fontWeight = FontWeight.Bold),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            backgroundColor = Color.Transparent,
                        ),
                        modifier = Modifier
                            .width(50.dp)
                            .fillMaxHeight()
                    )
                    TextField(
                        value = textLeftZeros,
                        onValueChange = {
                            if (it.length <= 3)
                                textLeftZeros = it
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        placeholder = { Text(stringResource(R.string.zeros)) },
                        textStyle = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            backgroundColor = Color.Transparent,
                        ),
                        modifier = Modifier
                            .width(80.dp)
                            .fillMaxHeight()
                    )
                    TextField(
                        value = textRightSymbol,
                        onValueChange = {
                            if (it.length <= 2)
                                textRightSymbol = it
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        placeholder = { Text(stringResource(R.string.aa)) },
                        textStyle = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            backgroundColor = Color.Transparent,
                        ),
                        modifier = Modifier
                            .width(70.dp)
                            .fillMaxHeight()
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .width(90.dp)
                        .fillMaxHeight()
                        .background(
                            Color.White,
                            shape = Shapes.small
                        )
                ) {
                    TextField(
                        value = textRightZeros,
                        onValueChange = {
                            if (it.length <= 3)
                                textRightZeros = it
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        placeholder = { Text(stringResource(R.string.zeros)) },
                        textStyle = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            backgroundColor = Color.Transparent,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    )
                    Text(
                        text = stringResource(R.string.rus),
                        fontSize = 20.sp,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
            Text(
                text = stringResource(R.string.other_number_format),
                style = Typography.body1,
                color = Color.Blue,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                ScreenTextfield(
                    value = textModel,
                    onValueChange = {
                        textModel = it
                    },
                    keyboardType = KeyboardType.Text,
                    label = { Text(stringResource(R.string.car_model))},
                    modifier = Modifier
                        .fillMaxWidth()
                )
                TextfieldUnderLine()
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                ScreenTextfield(
                    value = textPhone,
                    onValueChange = {
                        textPhone = it
                    },
                    keyboardType = KeyboardType.Phone,
                    label = { Text(stringResource(R.string.phone))},
                    modifier = Modifier
                        .fillMaxWidth()
                )
                TextfieldUnderLine()
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                ScreenTextfield(
                    value = textCard,
                    onValueChange = {
                        textCard = it
                    },
                    keyboardType = KeyboardType.Number,
                    label = { Text(stringResource(R.string.card))},
                    modifier = Modifier
                        .fillMaxWidth()
                )
                TextfieldUnderLine()
            }
        }

        TextButton(
            text = stringResource(R.string.create_account),
            onClick = {},
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInputDataScreen() {
    RegistrationExtensionDataContent()
}