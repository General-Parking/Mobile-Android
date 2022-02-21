package io.mishkav.generalparking.ui.utils

import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableStateFlow

sealed class Result<out T>(
    val data: T? = null,
    @StringRes val message: Int? = null,
    val exceptionMessage: String? = null
)

class SuccessResult<T>(data: T?) : Result<T>(data = data)
class LoadingResult<T>(data: T? = null) : Result<T>(data = data)
class ErrorResult<T>(
    message: Int? = null,
    exceptionMessage: String? = null
) : Result<T>(message = message, exceptionMessage = exceptionMessage)
class NothingResult<T> : Result<T>()

fun <T> MutableResultFlow(value: Result<T> = NothingResult()) = MutableStateFlow(value)
typealias MutableResultFlow<T> = MutableStateFlow<Result<T>>