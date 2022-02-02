package io.mishkav.generalparking.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.mishkav.generalparking.ui.theme.Typography


@Preview(showBackground = true)
@Composable
fun ConfirmEmailScreen() {
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
                text = "Подтверждение почты",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(textAlign = TextAlign.Center),
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .align(Alignment.TopCenter)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = "На почту aaaaaaaa@gmail.com пришло сообщение с дальнейшими инструкциями",
                fontSize = 18.sp,
                style = TextStyle(textAlign = TextAlign.Center),
                modifier = Modifier
                    .align(Alignment.TopCenter)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(3f)
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
                    text = "Подтверждена, продолжить",
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
                text = "Другой аккаунт?",
                fontSize = 16.sp
            )
            Text(
                text = "Войти",
                fontSize = 16.sp,
                color = Color(0xffffcc00),
                fontWeight = FontWeight.Bold
            )
        }
    }
}