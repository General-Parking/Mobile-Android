package io.mishkav.generalparking.domain.entities

interface TimeCallback {
    fun onCallback(value: String)
}

const val TIME_ZONE = "Atlantic/Reykjavik"