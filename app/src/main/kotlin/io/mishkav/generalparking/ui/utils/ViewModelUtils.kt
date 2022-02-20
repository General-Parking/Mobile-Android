package io.mishkav.generalparking.ui.utils

import io.mishkav.generalparking.R
import timber.log.Timber

inline fun <T> MutableResultFlow<T>.loadOrError(
    message: Int = R.string.basic_error,
    isLoadingResult: Boolean = true,
    load: () -> T?
) {
    if (isLoadingResult)
        value = LoadingResult()

    value = try {
        SuccessResult(load())
    } catch (e: Exception) {
        Timber.wtf(e)
        ErrorResult(message, e.message)
    }
}
