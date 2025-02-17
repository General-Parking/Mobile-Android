package io.mishkav.generalparking.ui.utils

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
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

class ValueEventListenerImpl(
    val onDataChangeImpl: (dataSnapshot: DataSnapshot) -> Unit,
    val onCancelledImpl: (error: DatabaseError) -> Unit
) : ValueEventListener {
    override fun onDataChange(snapshot: DataSnapshot) {
        onDataChangeImpl(snapshot)
    }

    override fun onCancelled(error: DatabaseError) {
        onCancelledImpl(error)
    }
}

fun DatabaseReference.onValueListener(
    onDataChangeImpl: (dataSnapshot: DataSnapshot) -> Unit,
    onCancelledImpl: (error: DatabaseError) -> Unit
) {
    addValueEventListener(
        ValueEventListenerImpl(onDataChangeImpl, onCancelledImpl)
    )
}
