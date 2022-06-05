package io.mishkav.generalparking.ui.screens.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.buttons.TextButton
import io.mishkav.generalparking.ui.components.loaders.CircularLoader
import io.mishkav.generalparking.ui.components.topAppBar.TopAppBarWithBackButton
import io.mishkav.generalparking.ui.theme.Shapes
import io.mishkav.generalparking.ui.utils.LoadingResult
import io.mishkav.generalparking.ui.utils.subscribeOnError

@Composable
fun PaymentScreen(
    navController: NavHostController,
    showMessage: (message: Int) -> Unit = {},
) {
    val viewModel: PaymentViewModel = viewModel()

    val balance by viewModel.balance.collectAsState()
    balance.subscribeOnError(showMessage)

    LaunchedEffect(Unit) {
        viewModel.onOpen()
    }

    PaymentScreenContent(
        balance = balance.data ?: 0,
        defaultPaymentPrice = 150,
        isLoading = balance is LoadingResult,
        navigateBack = {
            navController.popBackStack()
        }
    )
}

@Composable
fun PaymentScreenContent(
    balance: Int = 0,
    isLoading: Boolean = false,
    defaultPaymentPrice: Int = 0,
    navigateBack: () -> Unit = {}
) = Box(
    modifier = Modifier.fillMaxSize()
) {
    var paymentText by remember {
        mutableStateOf("$defaultPaymentPrice")
    }
    val horizontalPadding = 12.dp
    val colors = TextFieldDefaults.textFieldColors(
        textColor = MaterialTheme.colorScheme.onPrimary,
        cursorColor = MaterialTheme.colorScheme.primary,
        disabledLabelColor = MaterialTheme.colorScheme.primary,
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = MaterialTheme.colorScheme.primary,
        placeholderColor = MaterialTheme.colorScheme.onPrimary,
        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
        backgroundColor = Color.Transparent
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TopAppBarWithBackButton(
            title = {
                Text(
                    text = stringResource(R.string.account_refill),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            navigateBack = navigateBack
        )

        if (isLoading) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularLoader()
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding),
            border = BorderStroke(1.dp, Color.Black),
            shape = Shapes.medium
        ) {
            Column {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Текущий баланс: $balance ₽",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding),
            border = BorderStroke(1.dp, Color.Black),
            shape = Shapes.small
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Способ оплаты",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Обратите внимание на комиссию внешних сервисов",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                PaymentItem(
                    title = "Бесплатно",
                    description = "Коммисия 0%, минимум 0 ₽",
                    painter = painterResource(R.drawable.ic_gift)
                )

                Spacer(modifier = Modifier.height(32.dp))

                TextButton(
                    text = stringResource(R.string.edit),
                    onClick = {},
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding),
            border = BorderStroke(1.dp, Color.Black),
            shape = Shapes.small
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Введите сумму",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

                TextField(
                    value = paymentText,
                    onValueChange = { value ->
                        paymentText = value.filter { it.isDigit() }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.zero),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    textStyle = MaterialTheme.typography.titleLarge.copy(
                        textAlign = TextAlign.Center
                    ),
                    trailingIcon = {
                        Text(
                            text = stringResource(R.string.rub),
                            textAlign = TextAlign.Center
                        )
                    },
                    colors = colors,
                    modifier = Modifier
                        .height(60.dp)
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding),
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    TextButton(
        text = stringResource(R.string.pay),
        onClick = {},
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(horizontalPadding)
    )
}

@Composable
private fun PaymentItem(
    title: String,
    description: String,
    painter: Painter
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            painter = painter,
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = false)
@Composable
fun PaymentScreenPreview() {
    PaymentScreenContent()
}
