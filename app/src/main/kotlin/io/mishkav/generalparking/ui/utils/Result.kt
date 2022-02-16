package io.mishkav.generalparking.ui.utils

import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableStateFlow

sealed class Result<out T>(
    val data: T? = null,
    @StringRes val message: Int? = null
)

class SuccessResult<T>(data: T?) : Result<T>(data = data)
class ErrorResult<T>(message: Int?) : Result<T>(message = message)
class NothingResult<T> : Result<T>()

fun <T> MutableResultFlow(value: Result<T> = NothingResult()) = MutableStateFlow(value)
typealias MutableResultFlow<T> = MutableStateFlow<Result<T>>