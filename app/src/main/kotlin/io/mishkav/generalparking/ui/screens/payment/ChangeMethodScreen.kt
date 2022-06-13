package io.mishkav.generalparking.ui.screens.payment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.topAppBar.TopAppBarWithBackButton
import io.mishkav.generalparking.ui.screens.payment.components.PaymentItem
import io.mishkav.generalparking.ui.screens.payment.config.PaymentConfig
import io.mishkav.generalparking.ui.screens.payment.config.PaymentMethods
import io.mishkav.generalparking.ui.theme.Gray700
import io.mishkav.generalparking.ui.theme.Shapes
import io.mishkav.generalparking.ui.theme.Yellow400

@Composable
fun ChangeMethodScreen(
    navController: NavHostController,
) {
    val viewModel: PaymentViewModel = viewModel()

    val selectedOption by viewModel.selectedOption.collectAsState()

    ChangeMethodScreenContent(
        selectedOption = selectedOption,
        viewModel = viewModel,
        navigateBack = {
            navController.popBackStack()
        }
    )
}

@Composable
fun ChangeMethodScreenContent(
    selectedOption: String,
    viewModel: PaymentViewModel,
    navigateBack: () -> Unit = {}
) = Box(
    modifier = Modifier.fillMaxSize()
) {
    val horizontalPadding = 12.dp
    val verticalPadding = 64.dp

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TopAppBarWithBackButton(
            title = {
                Text(
                    text = stringResource(R.string.payment_methods),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            navigateBack = navigateBack
        )

        Spacer(modifier = Modifier.height(verticalPadding / 2))

        Column {

            PaymentConfig.paymentMethods.forEach { methodData ->
                val selected = selectedOption == methodData.title
                PaymentItem(
                    title = methodData.title,
                    description = methodData.description,
                    painter = painterResource(methodData.icon),
                    modifier = Modifier
                        .padding(horizontal = horizontalPadding)
                        .selectable(
                            selected = selected,
                            onClick = {
                                viewModel.changeSelected(methodData)
                                navigateBack()
                            }
                        )
                        .border(
                            border = when {
                                selected -> BorderStroke(3.dp, SolidColor(Yellow400))
                                else -> BorderStroke(1.dp, SolidColor(Gray700))
                            },
                            shape = Shapes.small
                        )
                )

                Spacer(modifier = Modifier.height(verticalPadding / 4))
            }
        }
    }
}