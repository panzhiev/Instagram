package com.panzhiev.instagram.activity

import android.content.Context
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.time.Duration

class ValueEventListenerAdapter(val handler: (DataSnapshot) -> Unit) : ValueEventListener {

    private val TAG = "ValueEventListenerAdapt"

    override fun onCancelled(p0: DatabaseError?) {}

    override fun onDataChange(data: DataSnapshot) {
        handler(data)
    }
}

fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}