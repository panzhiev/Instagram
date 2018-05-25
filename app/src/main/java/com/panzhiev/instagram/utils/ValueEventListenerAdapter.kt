package com.panzhiev.instagram.utils

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ValueEventListenerAdapter(val handler: (DataSnapshot) -> Unit) : ValueEventListener {

    private val TAG = "ValueEventListenerAdapt"

    override fun onCancelled(p0: DatabaseError?) {}

    override fun onDataChange(data: DataSnapshot) {
        handler(data)
    }
}