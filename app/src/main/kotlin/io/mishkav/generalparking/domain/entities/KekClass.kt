package io.mishkav.generalparking.domain.entities

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

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
