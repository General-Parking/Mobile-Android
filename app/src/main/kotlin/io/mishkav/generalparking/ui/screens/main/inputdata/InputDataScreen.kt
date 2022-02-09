package io.mishkav.generalparking.ui.screens.main.inputdata

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.mishkav.generalparking.ui.theme.Typography
import androidx.compose.ui.res.stringResource
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.ScreenTextfield
import io.mishkav.generalparking.ui.components.buttons.LowerButton
import io.mishkav.generalparking.ui.components.lines.TextfieldUnderLine
import io.mishkav.generalparking.ui.components.texts.ScreenBody
import io.mishkav.generalparking.ui.components.texts.ScreenTitle

@Composable
fun InputDataScreen() {

    var textEdit4 by rememberSaveable { mutableStateOf("") }
    var textEdit1 by rememberSaveable { mutableStateOf("") }
    var textEdit2 by rememberSaveable { mutableStateOf("") }
    var textEdit3 by rememberSaveable { mutableStateOf("") }
    var textModel by rememberSaveable { mutableStateOf("") }
    var textPhone by rememberSaveable { mutableStateOf("") }
    var textCard by rememberSaveable { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 40.dp)
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
                .weight(1.2f)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(300.dp)
                    .height(60.dp)
                    .background(
                        Color.Black,
                        shape = MaterialTheme.shapes.medium
                    )
                    .align(Alignment.CenterHorizontally)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .width(200.dp)
                        .fillMaxHeight()
                        .background(
                            Color.White,
                            shape = MaterialTheme.shapes.medium
                        )
                ) {

                    TextField(
                        value = textEdit1,
                        onValueChange = {
                            if (it.length <= 1)
                                textEdit1 = it
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        placeholder = { Text(stringResource(R.string.a)) },
                        textStyle = TextStyle(fontSize = 30.sp),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            backgroundColor = Color.Transparent,
                        ),
                        modifier = Modifier
                            .align(Alignment.Bottom)
                            .width(50.dp)
                            .fillMaxHeight()
                    )
                    TextField(
                        value = textEdit2,
                        onValueChange = {
                            if (it.length <= 3)
                                textEdit2 = it
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        placeholder = { Text(stringResource(R.string.zeros)) },
                        textStyle = TextStyle(fontSize = 40.sp),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            backgroundColor = Color.Transparent,
                        ),
                        modifier = Modifier
                            .align(Alignment.Bottom)
                            .width(60.dp)
                            .fillMaxHeight()
                    )
                    TextField(
                        value = textEdit3,
                        onValueChange = {
                            if (it.length <= 2)
                                textEdit3 = it
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        placeholder = { Text(stringResource(R.string.aa)) },
                        textStyle = TextStyle(fontSize = 30.sp),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            backgroundColor = Color.Transparent,
                        ),
                        modifier = Modifier
                            .align(Alignment.Bottom)
                            .width(60.dp)
                            .fillMaxHeight()
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .width(90.dp)
                        .fillMaxHeight()
                        .background(
                            Color.White,
                            shape = MaterialTheme.shapes.medium
                        )
                ) {
                    TextField(
                        value = textEdit4,
                        onValueChange = {
                            if (it.length <= 3)
                                textEdit4 = it
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        placeholder = { Text(stringResource(R.string.zeros)) },
                        textStyle = TextStyle(fontSize = 30.sp),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            backgroundColor = Color.Transparent,
                        ),
                        modifier = Modifier
                            .width(60.dp)
                    )

                    Text(
                        text = stringResource(R.string.rus),
                        style = TextStyle(textAlign = TextAlign.Center)
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
                .weight(2f)
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
                    label = stringResource(R.string.car_model),
                    placeholder = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 1.dp)
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
                    label = stringResource(R.string.phone),
                    placeholder = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 1.dp)
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
                    label = stringResource(R.string.card),
                    placeholder = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 1.dp)
                )
                TextfieldUnderLine()
            }
        }

        LowerButton(
            text = stringResource(R.string.create_account),
            onClick = {},
            modifier = Modifier.weight(3f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInputDataScreen() {
    InputDataScreen()
}