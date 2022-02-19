package io.mishkav.generalparking.ui.utils

import kotlinx.coroutines.flow.MutableStateFlow

sealed class Result<out T>(
    val data: T? = null,
    val message: String? = null
)

class SuccessResult<T>(data: T?) : Result<T>(data = data)
class LoadingResult<T>(data: T? = null) : Result<T>(data = data)
class ErrorResult<T>(message: String?) : Result<T>(message = message)
class NothingResult<T> : Result<T>()

fun <T> MutableResultFlow(value: Result<T> = NothingResult()) = MutableStateFlow(value)
typealias MutableResultFlow<T> = MutableStateFlow<Result<T>>