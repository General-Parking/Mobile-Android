package io.mishkav.generalparking.data.exceptions

class EmailNotVerifiedException(
    message: String = "Email is not verified"
) : Exception(message)