package io.mishkav.generalparking.ui.utils

import androidx.annotation.StringRes

sealed class Result <out T>(
    val data: T? = null,
    val message: String? = null
)

class SuccessResult<T>(data: T?) : Result<T>(data = data)
class ErrorResult<T>(message: String?) : Result<T>(message = message)