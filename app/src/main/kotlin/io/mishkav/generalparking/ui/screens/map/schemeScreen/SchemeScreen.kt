package io.mishkav.generalparking.ui.screens.map.schemeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.insets.systemBarsPadding
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.ReservedSchemeContent
import io.mishkav.generalparking.ui.components.SelectedSchemeContent
import io.mishkav.generalparking.ui.components.UnselectedSchemeContent
import io.mishkav.generalparking.ui.components.texts.BottomBody
import io.mishkav.generalparking.ui.components.texts.BottomTitle
import io.mishkav.generalparking.ui.theme.GeneralParkingTheme

@Composable
fun SchemeScreen(
    navController: NavHostController,
    onError: @Composable (Int) -> Unit
) {
    val viewModel: SchemeViewModel = viewModel()
    val currentParkingAddress by viewModel.currentParkingAddress.collectAsState()

    SchemeScreenContent(
        textAddress = currentParkingAddress
    )
}

@Composable
fun SchemeScreenContent(
    textAddress: String = stringResource(R.string.bottom_title)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        BottomTitle(
            text = textAddress,
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(R.dimen.bottom_padding)
                )
        )
        BottomBody(
            text = stringResource(R.string.parking_scheme),
            modifier = Modifier
                .padding(
                    horizontal = dimensionResource(R.dimen.bottom_padding)
                )
        )
        Box(Modifier.weight(1f).background(color = Color.Black))
        ReservedSchemeContent()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSchemeScreen() {
    GeneralParkingTheme {
        SchemeScreenContent()
    }
}