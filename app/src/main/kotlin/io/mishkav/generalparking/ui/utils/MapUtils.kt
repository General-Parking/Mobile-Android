package io.mishkav.generalparking.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.MapStyleOptions
import io.mishkav.generalparking.ui.utils.GoogleMapStylePath.NIGHT
import timber.log.Timber
import java.io.IOException

@Composable
fun getGoogleMapStyleOption(): MapStyleOptions? {
    val context = LocalContext.current
    var jsonString: String? = null

    try {
        jsonString = context.assets.open(NIGHT)
            .bufferedReader()
            .use { it.readText() }
    } catch (ioException: IOException) {
        Timber.d(ioException.message)
    }

    return jsonString?.let { MapStyleOptions(it) }
}

object GoogleMapStylePath {
    const val NIGHT = "google_map_night.json"
}