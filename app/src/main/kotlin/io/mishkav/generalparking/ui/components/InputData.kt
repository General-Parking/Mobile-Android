package io.mishkav.generalparking.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.mishkav.generalparking.ui.theme.Typography

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Preview(showBackground = true)
@Composable
fun InputDataScreen() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 40.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = "Регистрация",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .align(Alignment.BottomCenter)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = "Добавьте информацию, чтобы начать пользоваться приложением.",
                fontSize = 18.sp,
                style = TextStyle(textAlign = TextAlign.Center),
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
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
                        Color(0xff000000),
                        shape = RoundedCornerShape(25)
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
                            Color(0xffffffff),
                            shape = RoundedCornerShape(25)
                        )
                ) {
                    var textEdit1 by rememberSaveable { mutableStateOf("") }
                    var textEdit2 by rememberSaveable { mutableStateOf("") }
                    var textEdit3 by rememberSaveable { mutableStateOf("") }

                    TextField(
                        value = textEdit1,
                        onValueChange = {
                            if (it.length <= 1)
                                textEdit1 = it
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        placeholder = { Text("А") },
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
                        placeholder = { Text("000") },
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
                        placeholder = { Text("АА") },
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
                var textEdit4 by rememberSaveable { mutableStateOf("") }
                Row(
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .width(90.dp)
                        .fillMaxHeight()
                        .background(
                            Color(0xffffffff),
                            shape = RoundedCornerShape(25)
                        )
                ) {
                    TextField(
                        value = textEdit4,
                        onValueChange = {
                            if (it.length <= 3)
                                textEdit4 = it
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        placeholder = { Text("000") },
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
                        text = "RUS",
                        style = TextStyle(textAlign = TextAlign.Center)
                    )
                }
            }
            Text(
                text = "У меня другой формат номера",
                style = TextStyle(textAlign = TextAlign.Center),
                color = Color(0xff0000ff),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
        var textModel by rememberSaveable { mutableStateOf("") }
        var textPhone by rememberSaveable { mutableStateOf("") }
        var textCard by rememberSaveable { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextField(
                    value = textModel,
                    onValueChange = {
                        textModel = it
                    },
                    label = { Text("Марка автомобиля") },
                    textStyle = Typography.body1,
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        backgroundColor = Color.Transparent,
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 1.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(color = Color(0xaaaaaaaa))
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextField(
                    value = textPhone,
                    singleLine = true,
                    onValueChange = {
                        textPhone = it
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    label = { Text("Номер телефона") },
                    textStyle = Typography.body1,
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        backgroundColor = Color.Transparent,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 1.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(color = Color(0xaaaaaaaa))
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextField(
                    value = textCard,
                    onValueChange = {
                        textCard = it
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text("Банковская карта") },
                    textStyle = Typography.body1,
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        backgroundColor = Color.Transparent,
                    ),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 1.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(color = Color(0xaaaaaaaa))
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f)
        ) {
            Button(
                onClick = { },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xffffcc00)),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)


            ) {
                Text(
                    text = "Создать аккаунт",
                    color = Color(0xffffffff),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}