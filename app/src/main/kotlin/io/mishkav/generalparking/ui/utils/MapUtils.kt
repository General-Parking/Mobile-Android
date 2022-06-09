package io.mishkav.generalparking.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.MapStyleOptions
import io.mishkav.generalparking.ui.utils.MapParameters.GOOGLE_NIGHT_PATH
import timber.log.Timber
import java.io.IOException

@Composable
fun getGoogleMapStyleOption(): MapStyleOptions? {
    val context = LocalContext.current
    var jsonString: String? = null

    try {
        jsonString = context.assets.open(GOOGLE_NIGHT_PATH)
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

object MapParameters {
    const val GOOGLE_NIGHT_PATH = "google_map_night.json"
    const val TIME_ZONE = "Atlantic/Reykjavik"
}