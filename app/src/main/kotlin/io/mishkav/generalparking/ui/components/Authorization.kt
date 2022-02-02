package io.mishkav.generalparking.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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


@Preview(showBackground = true)
@Composable
fun AuthScreen() {
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
                .weight(2f)
        ) {
            Text(
                text = "Авторизация",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .align(Alignment.BottomCenter)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = "Добро пожаловать! Войдите в систему, чтобы начать пользоваться приложением",
                fontSize = 18.sp,
                style = TextStyle(textAlign = TextAlign.Center),
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
        var textEmail by rememberSaveable { mutableStateOf("") }
        var textPassword by rememberSaveable { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.5f)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                TextField(
                    value = textEmail,
                    singleLine = true,
                    onValueChange = {
                        textEmail = it
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    label = { Text("Email") },
                    textStyle = Typography.body1,
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        backgroundColor = Color.Transparent,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)

                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(color = Color(0xaaaaaaaa))
                )
            }
            Column (
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    TextField(
                        value = textPassword,
                        onValueChange = {
                            textPassword = it
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        label = { Text("Пароль") },
                        textStyle = Typography.body1,
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            backgroundColor = Color.Transparent,
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .padding(vertical = 1.dp)

                    )
                    Text(
                        text = "Забыли пароль?",
                        fontSize = 15.sp,
                        color = Color(0xffffcc00),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(vertical = 15.dp)

                    )
                }
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
                .weight(2f)
        ) {
            Button (
                onClick = { },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xffffcc00)),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)


            ) {
                Text(
                    text = "Войти",
                    color = Color(0xffffffff),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 30.dp)
                .fillMaxWidth()

        ) {
            Text(
                text = "Нет аккаунта?",
                fontSize = 16.sp
            )
            Text(
                text = "Создать",
                fontSize = 16.sp,
                color = Color(0xffffcc00),
                fontWeight = FontWeight.Bold
            )
        }
    }
}