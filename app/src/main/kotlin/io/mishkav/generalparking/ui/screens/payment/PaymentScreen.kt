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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.buttons.TextButton
import io.mishkav.generalparking.ui.components.loaders.CircularLoader
import io.mishkav.generalparking.ui.components.texts.ScreenBody
import io.mishkav.generalparking.ui.components.topAppBar.TopAppBarWithBackButton
import io.mishkav.generalparking.ui.screens.main.Routes
import io.mishkav.generalparking.ui.screens.payment.components.PaymentItem
import io.mishkav.generalparking.ui.screens.payment.config.PaymentConfig
import io.mishkav.generalparking.ui.screens.payment.config.PaymentMethods
import io.mishkav.generalparking.ui.theme.Shapes
import io.mishkav.generalparking.ui.utils.LoadingResult
import io.mishkav.generalparking.ui.utils.SuccessResult
import io.mishkav.generalparking.ui.utils.subscribeOnError

@Composable
fun PaymentScreen(
    navController: NavHostController,
    showMessage: (message: Int) -> Unit = {},
) {
    val viewModel: PaymentViewModel = viewModel()

    val balance by viewModel.balance.collectAsState()
    balance.subscribeOnError(showMessage)

    val giftResult by viewModel.giftResult.collectAsState()
    giftResult.subscribeOnError(showMessage)

    val selectedOption by viewModel.selectedOption.collectAsState()

    var isSuccessPaymentDialogVisible by remember {
        mutableStateOf(false)
    }
    giftResult.takeIf { it is SuccessResult }?.data?.let {
        LaunchedEffect(Unit) {
            isSuccessPaymentDialogVisible = true
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onOpen()
    }

    PaymentScreenContent(
        balance = balance.data ?: 0,
        defaultPaymentPrice = 150,
        selectedOption = selectedOption,
        isLoading = balance is LoadingResult || giftResult is LoadingResult,
        onPaying = viewModel::setGiftBalance,
        navigateBack = {
            navController.popBackStack()
        },
        navigateToChange = {
            navController.navigate(Routes.changeMethod)
        }
    )

    if (isSuccessPaymentDialogVisible) {
        AlertDialog(
            onDismissRequest = {},
            shape = RoundedCornerShape(
                dimensionResource(R.dimen.bottom_shape),
            ),
            text = {
                ScreenBody(
                    text = stringResource(R.string.payment_success_info)
                )
            },
            backgroundColor = MaterialTheme.colorScheme.background,
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { navController.popBackStack() }
                    ) {
                        Text(
                            text = stringResource(R.string.good_text)
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun PaymentScreenContent(
    balance: Int = 0,
    isLoading: Boolean = false,
    defaultPaymentPrice: Int = 0,
    selectedOption: PaymentMethods = PaymentConfig.paymentMethods[1],
    onPaying: (newBalance: Int) -> Unit = { _ -> },
    navigateBack: () -> Unit = {},
    navigateToChange: () -> Unit = {}
) = Box(
    modifier = Modifier.fillMaxSize()
) {
    var paymentText by remember {
        mutableStateOf("$defaultPaymentPrice")
    }
    val horizontalPadding = 12.dp
    val verticalPadding = 64.dp
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

        Spacer(modifier = Modifier.height(verticalPadding / 2))

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
                    style = MaterialTheme.typography.bodyMedium.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 32.sp
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            textStyle = MaterialTheme.typography.titleMedium.copy(
                textAlign = TextAlign.Center,
                fontSize = 32.sp
            ),
            trailingIcon = {
                Text(
                    text = stringResource(R.string.rub),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 32.sp
                    )
                )
            },
            colors = colors,
            modifier = Modifier
                // .height(60.dp)
                .width(LocalConfiguration.current.screenWidthDp.dp / 2)
                .padding(horizontal = horizontalPadding),
        )

        Spacer(modifier = Modifier.height(verticalPadding / 4))

        Text(
            text = stringResource(R.string.balance_after_replenishment).format(
                if (paymentText.isNotEmpty())
                    balance + paymentText.toInt()
                else
                    balance
            ),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = horizontalPadding)
        )

        Spacer(modifier = Modifier.height(verticalPadding))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding),
            border = BorderStroke(1.dp, Color.Black),
            shape = Shapes.small
        ) {
            Column {
                Spacer(modifier = Modifier.height(verticalPadding / 4))

                Text(
                    text = stringResource(R.string.payment_method),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                )

                Spacer(modifier = Modifier.height(verticalPadding / 2))

                // TODO Choose depends on selected method
                PaymentItem(
                    title = selectedOption.title,
                    description = selectedOption.description,
                    painter = painterResource(selectedOption.icon)
                )

                Spacer(modifier = Modifier.height(verticalPadding / 2))

                TextButton(
                    text = stringResource(R.string.edit),
                    onClick = navigateToChange,
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                )

                Spacer(modifier = Modifier.height(verticalPadding / 4))
            }
        }

        Spacer(modifier = Modifier.height(verticalPadding / 4))

        Text(
            text = stringResource(R.string.payment_info),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = horizontalPadding)
        )

        Spacer(modifier = Modifier.height(verticalPadding / 4))
    }

    TextButton(
        text = stringResource(R.string.pay),
        onClick = {
            onPaying(
                if (paymentText.isNotEmpty())
                    balance + paymentText.toInt()
                else
                    balance
            )
        },
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(horizontalPadding)
    )
}

@Preview(showBackground = false)
@Composable
fun PaymentScreenPreview() {
    PaymentScreenContent()
}
