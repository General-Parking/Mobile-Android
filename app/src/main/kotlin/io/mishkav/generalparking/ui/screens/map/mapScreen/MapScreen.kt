package io.mishkav.generalparking.ui.screens.map.mapScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import io.mishkav.generalparking.R

@Composable
fun MapScreen(
    navController: NavHostController,
    onError: @Composable (Int) -> Unit
) {
    MapScreenContent(

    )
}

@Composable
fun MapScreenContent(

) {
    val singapore = LatLng(55.754001, 37.649003)
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        googleMapOptionsFactory = {
            GoogleMapOptions().camera(CameraPosition.fromLatLngZoom(singapore, 11f))
        }
    ) {
        Marker(
            position = singapore,
            icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)
        )
    }
}

@Composable
fun MapScreenPreview(

) {

}