package io.mishkav.generalparking.ui.utils

import androidx.annotation.StringRes
import io.mishkav.generalparking.R
import timber.log.Timber

inline fun <T> MutableResultFlow<T>.loadOrError(
    @StringRes messageRes: Int = R.string.basic_error,
    load: () -> T?
) {
    value = try {
        SuccessResult(load())
    } catch (e: Exception) {
        Timber.wtf(e)
        ErrorResult(messageRes)
    }
}
