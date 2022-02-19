package io.mishkav.generalparking.ui.utils

import timber.log.Timber

inline fun <T> MutableResultFlow<T>.loadOrError(
    message: String = "basic error",
    isLoadingResult: Boolean = true,
    isDefaultMessage: Boolean = false,
    load: () -> T?
) {
    if (isLoadingResult)
        value = LoadingResult()

    value = try {
        SuccessResult(load())
    } catch (e: Exception) {
        Timber.wtf(e)
        if (isDefaultMessage)
            ErrorResult(message)
        else
            ErrorResult(e.message)
    }
}
