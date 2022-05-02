package io.mishkav.generalparking.data.exceptions

import androidx.annotation.StringRes
import io.mishkav.generalparking.R

class EmailNotVerifiedException(
    @StringRes messageRes: Int = R.string.error_email_not_verified
) : Exception(messageRes.toString())


class NullUserException(
    @StringRes messageRes: Int = R.string.error_auth
) : Exception(messageRes.toString())

class PlaceReservationException(
    @StringRes messageRes: Int = R.string.error_place_reserved
) : Exception(messageRes.toString())

class PlaceNotReservatedException(
    @StringRes messageRes: Int = R.string.error_place_reserved
) : Exception(messageRes.toString())

class MinSdkVersionException(
    @StringRes messageRes: Int = R.string.error_min_sdk_version
) : Exception(messageRes.toString())