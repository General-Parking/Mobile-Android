package io.mishkav.generalparking.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

// Error functions
@Composable
inline fun Result<*>.subscribeOnError(crossinline onError: (message: Int) -> Unit) = (this as? ErrorResult)?.message?.let {
    LaunchedEffect(this) {
        onError(it)
    }
}

// content: @Composable () -> Unit
@Composable
inline fun Result<*>.subscribeOnErrorMax(onError: @Composable (message: Int) -> Unit) =
    (this as? ErrorResult)?.message?.let {
        onError(it)
    }

object GoogleMapStylePath {
    const val NIGHT = "google_map_night.json"
}