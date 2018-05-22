package com.panzhiev.instagram.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.panzhiev.instagram.R
import com.panzhiev.instagram.models.User
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    private val TAG = "EditProfileActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        close_image.setOnClickListener {
            finish()
        }

        val auth = FirebaseAuth.getInstance()
        val database = FirebaseDatabase.getInstance().reference
        database.child("users").child(auth.currentUser!!.uid)
                .addListenerForSingleValueEvent(ValueEventListenerAdapter {

                    val user = it.getValue(User::class.java)
                    et_name.setText(user!!.name, TextView.BufferType.EDITABLE)
                    et_username.setText((user.username), TextView.BufferType.EDITABLE)
                    et_website.setText((user.website), TextView.BufferType.EDITABLE)
                    et_bio.setText((user.bio), TextView.BufferType.EDITABLE)
                    et_email.setText((user.email), TextView.BufferType.EDITABLE)
                    et_phone.setText((user.phone), TextView.BufferType.EDITABLE)

                })
    }
}
