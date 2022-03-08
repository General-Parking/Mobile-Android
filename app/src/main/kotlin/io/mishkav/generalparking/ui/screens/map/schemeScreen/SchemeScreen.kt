package io.mishkav.generalparking.ui.screens.map.schemeScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.google.accompanist.insets.systemBarsPadding
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.UnselectedSchemeContent
import io.mishkav.generalparking.ui.components.texts.BottomBody
import io.mishkav.generalparking.ui.components.texts.BottomTitle
import io.mishkav.generalparking.ui.theme.GeneralParkingTheme

@Composable
fun SchemeScreen(
    navController: NavHostController,
    onError: @Composable (Int) -> Unit
) {

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SchemeScreenContent(
    SchemeTitle: String = stringResource(R.string.bottom_title)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        BottomTitle(
            text = SchemeTitle
        )
        BottomBody(
            text = stringResource(R.string.parking_scheme)
        )
        Box(Modifier.weight(4f))
        UnselectedSchemeContent()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSchemeScreen() {
    GeneralParkingTheme {
        SchemeScreenContent()
    }
}