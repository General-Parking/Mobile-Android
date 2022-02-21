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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import io.mishkav.generalparking.ui.screens.auth.AuthViewModel
import io.mishkav.generalparking.ui.theme.GeneralParkingTheme
import io.mishkav.generalparking.ui.utils.ErrorResult
import io.mishkav.generalparking.ui.utils.LoadingResult
import io.mishkav.generalparking.ui.utils.SuccessResult

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

    val result by viewModel.signInResult.collectAsState()

    val text: String = when (result) {
        is SuccessResult -> "success"
        is ErrorResult -> "error"
        is LoadingResult -> "loading"
        else -> "wtf"
    }
    Column {
        Text(
            text = "Hello world!\n" +
                    text
        )

        Button(
            onClick = {
                viewModel.signIn("test@test.test", "123456")
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