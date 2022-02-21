package io.mishkav.generalparking.ui.screens.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import io.mishkav.generalparking.ui.screens.auth.AuthViewModel
import io.mishkav.generalparking.ui.theme.GeneralParkingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GeneralParkingTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {

    val viewModel: AuthViewModel = viewModel()

    val signInResult by viewModel.signInResult.collectAsState()
    val createUserResult by viewModel.createNewUserResult.collectAsState()

    val email = "mvorozhtsov@bk.ru"
    val password = "123456"

    Column {
        Text(
            text = "Hello world!\n" +
                "sign in = ${signInResult}\n" +
                "create user = ${createUserResult}\n" +
                "${createUserResult.message?.let { stringResource(id = it) }}\n" +
                "${createUserResult.exceptionMessage}"
        )

        Button(
            onClick = {
                viewModel.createNewUser(email, password)
            }
        ) {
            Text("Create")
        }

        Button(
            onClick = {
                viewModel.signIn(email, password)
            }
        ) {
            Text("Auth")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GeneralParkingTheme {
        Greeting("Android")
    }
}