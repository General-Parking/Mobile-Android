package io.mishkav.generalparking.ui.screens.map.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.dp
import io.mishkav.generalparking.R
import io.mishkav.generalparking.ui.components.buttons.TextButton
import io.mishkav.generalparking.ui.components.texts.ScreenTitle
import io.mishkav.generalparking.ui.theme.Gray200
import io.mishkav.generalparking.ui.theme.Typography
import io.mishkav.generalparking.ui.theme.Yellow500
import io.mishkav.generalparking.ui.theme.generalParkingDarkBackground
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

@Composable
fun ExitAlert(
    timeExitResult: String,
    timeArriveResult: String,
    timeReservationResult: String,
    priceParking: Long,
    bookingRatio: Double,
    onClick: () -> Unit = {}
) {

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
    val timeExit = LocalDateTime.parse(timeExitResult, formatter)
    val timeArrive = LocalDateTime.parse(timeArriveResult, formatter)
    val timeReservation = LocalDateTime.parse(timeReservationResult, formatter)

    var resultParkingPrice = (priceParking / 60).toInt() * abs(Duration.between(timeArrive, timeExit).toMinutes()).toInt()
    val retentionDiff = abs(Duration.between(timeReservation, timeArrive).toMinutes()) - 60
    var resultRetentionPrice = ((priceParking / 60).toInt() * bookingRatio * retentionDiff).toInt()

    AlertDialog(
        onDismissRequest = {},
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                ScreenTitle(
                    text = stringResource(R.string.see_you_again),
                    modifier = Modifier
                        .padding(vertical = dimensionResource(R.dimen.standard_padding))
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(vertical = dimensionResource(R.dimen.standard_padding))
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(
                                dimensionResource(R.dimen.bottom_shape),
                            )
                        )
                        .background(Gray200)
                        .padding(dimensionResource(R.dimen.standard_padding))
                ) {
                    Text(
                        text = stringResource(R.string.time_exit_alert),
                        color = generalParkingDarkBackground,
                        style = Typography.body1
                    )
                    Text(
                        text = "${abs(Duration.between(timeArrive, timeExit).toMinutes())} ${stringResource(R.string.minutes_exit_alert)}",
                        color = generalParkingDarkBackground,
                        style = Typography.body1
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(
                            RoundedCornerShape(
                                dimensionResource(R.dimen.bottom_shape),
                            )
                        )
                        .background(Gray200)
                        .padding(dimensionResource(R.dimen.standard_padding))
                ) {
                    Text(
                        text = stringResource(R.string.price_exit_alert),
                        color = generalParkingDarkBackground,
                        style = Typography.body1
                    )
                    Text(
                        text = when {
                            retentionDiff > 0 ->
                                AnnotatedString(
                                    text = "$resultParkingPrice₽"
                                ).plus(
                                    AnnotatedString(
                                        text = " + $resultRetentionPrice₽",
                                        spanStyle = SpanStyle(Yellow500)
                                    )
                                )
                            else -> AnnotatedString(
                                text = "$resultParkingPrice₽"
                            )
                        },
                        color = generalParkingDarkBackground,
                        style = Typography.body1
                    )
                    Spacer(Modifier.height(20.dp))
                }
            }
        },
        backgroundColor = MaterialTheme.colorScheme.background,
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.finish),
                    onClick = onClick
                )
            }
        }
    )
}